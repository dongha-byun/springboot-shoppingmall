package springboot.shoppingmall.order.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.OrderCodeCreator;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.dto.DeliveryInfoRequest;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.service.dto.DeliveryInfoCreateDto;
import springboot.shoppingmall.order.service.dto.OrderCreateDto;
import springboot.shoppingmall.order.service.dto.OrderDto;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final ProductFinder productFinder;
    private final UserCouponRepository userCouponRepository;
    private final OrderRepository orderRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final OrderCodeCreator orderCodeCreator;
    private final OrderUserInformationService orderUserInformationService;

    @Transactional
    public OrderDto createOrder(Long userId, OrderCreateDto orderCreateDto) {
        DeliveryInfoCreateDto deliveryInfoCreateDto = orderCreateDto.getDeliveryInfo();
        List<OrderItem> items = getOrderItems(orderCreateDto);

        // 주문 정보를 생성한다.
        String orderCode = orderCodeCreator.createOrderCode();
        OrderDeliveryInfo orderDeliveryInfo = deliveryInfoCreateDto.toValue();
        Order newOrder = Order.createOrder(orderCode, userId, items, orderDeliveryInfo);

        // 회원등급 할인 금액 적용
        // 주문자 할인율 조회, 구매자 정보 조회 OrderUserInfo
        // 사용자 ID 주고, 할인율 받아오면, 할인 계산할때 사용
        int discountRate = orderUserInformationService.getOrderUserDiscountRate(userId);
        newOrder.gradeDiscount(discountRate);

        // 쿠폰에 따른 할인 금액 적용
        newOrder.getItems()
                .stream()
                .filter(item -> item.getUsedUserCouponId()!=null)
                .forEach(this::discountByCoupon);

        // 실제 결제금액 산정
        newOrder.calculateRealPayPrice();
        orderRepository.save(newOrder);

        // 주문 정보 저장 시, 결제정보도 같이 저장한다.
        payHistoryRepository.save(
                PayHistory.create(
                        newOrder.getId(), orderCreateDto.getPayType().name(),
                        orderCreateDto.getTid(), newOrder.getRealPayPrice()
                )
        );

        return OrderDto.of(newOrder);
    }

    private void discountByCoupon(OrderItem item) {
        UserCoupon userCoupon = userCouponRepository.findById(item.getUsedUserCouponId()).orElseThrow();

        item.couponDiscount(userCoupon.ofDiscountRate());
        userCoupon.use();
    }

    private List<OrderItem> getOrderItems(OrderCreateDto orderCreateDto) {
        return orderCreateDto.getItems()
                .stream()
                .map(item -> {
                    Product product = productFinder.findProductById(item.getProductId());
                    product.validateQuantity(item.getQuantity());

                    return OrderItem.createOrderItem(product, item.getQuantity(), item.getUsedCouponId());
                })
                .collect(Collectors.toList());
    }
}
