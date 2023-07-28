package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.OrderCodeCreator;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderSequence;
import springboot.shoppingmall.order.domain.OrderSequenceRepository;
import springboot.shoppingmall.order.dto.DeliveryInfoRequest;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.dto.OrderItemRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final ProductFinder productFinder;
    private final UserFinder userFinder;
    private final UserCouponRepository userCouponRepository;
    private final OrderRepository orderRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final OrderCodeCreator orderCodeCreator;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        DeliveryInfoRequest deliveryInfoRequest = orderRequest.getDeliveryInfo();
        List<OrderItem> items = getOrderItems(orderRequest);

        // 주문 정보를 생성한다.
        String orderCode = orderCodeCreator.createOrderCode();
        OrderDeliveryInfo orderDeliveryInfo = deliveryInfoRequest.toValue();
        Order newOrder = orderRepository.save(
                Order.createOrder(orderCode, userId, items, orderDeliveryInfo)
        );

        // 회원등급 할인 금액 적용
        User user = userFinder.findUserById(userId);
        newOrder.gradeDiscount(user.discountRate());

        // 쿠폰에 따른 할인 금액 적용
        newOrder.getItems()
                .stream()
                .filter(item -> item.getUsedUserCouponId()!=null)
                .forEach(item -> {
                    Long userCouponId = item.getUsedUserCouponId();
                    Optional<UserCoupon> userCouponOptional = userCouponRepository.findById(userCouponId);
                    if (userCouponOptional.isPresent()) {
                        UserCoupon userCoupon = userCouponOptional.get();
                        Coupon coupon = userCoupon.getCoupon();
                        item.couponDiscount(coupon.getDiscountRate());
                        userCoupon.use();
                    }
                });

        newOrder.calculateRealPayPrice();

        // 주문 정보 저장 시, 결제정보도 같이 저장한다.
        payHistoryRepository.save(
                new PayHistory(
                        newOrder.getId(), orderRequest.getPayType(), orderRequest.getTid(), newOrder.getTotalPrice()
                )
        );

        return OrderResponse.of(newOrder);
    }

    private List<OrderItem> getOrderItems(OrderRequest orderRequest) {
        return orderRequest.getItems()
                .stream()
                .map(item -> {
                    Product product = productFinder.findProductById(item.getProductId());
                    product.validateQuantity(item.getQuantity());

                    return OrderItem.createOrderItem(product, item.getQuantity(), item.getUserCouponId());
                })
                .collect(Collectors.toList());
    }
}
