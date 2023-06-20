package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
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
    private final OrderRepository orderRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final OrderSequenceRepository orderSequenceRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        DeliveryInfoRequest deliveryInfoRequest = orderRequest.getDeliveryInfo();
        List<OrderItem> items = getOrderItems(orderRequest);

        String orderCode = generateOrderCode();
        Order newOrder = orderRepository.save(
                Order.createOrder(orderCode, userId, items,
                        deliveryInfoRequest.getReceiverName(), deliveryInfoRequest.getReceiverPhoneNumber(),
                        deliveryInfoRequest.getZipCode(), deliveryInfoRequest.getAddress(),
                        deliveryInfoRequest.getDetailAddress(), deliveryInfoRequest.getRequestMessage())
        );

        // 회원등급 할인 금액 적용
        User user = userFinder.findUserById(userId);
        newOrder.gradeDiscount(user.discountRate());

        // 주문 정보 저장 시, 결제정보도 같이 저장한다.
        payHistoryRepository.save(
                new PayHistory(
                        newOrder.getId(), orderRequest.getPayType(), orderRequest.getTid(), newOrder.getTotalPrice()
                )
        );

        return OrderResponse.of(newOrder);
    }

    private List<OrderItem> getOrderItems(OrderRequest orderRequest) {
        List<OrderItem> items = new ArrayList<>();
        List<OrderItemRequest> itemRequests = orderRequest.getItems();
        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = productFinder.findProductById(itemRequest.getProductId());
            product.validateQuantity(itemRequest.getQuantity());

            items.add(OrderItem.createOrderItem(product, itemRequest.getQuantity()));
        }

        return items;
    }

    private String generateOrderCode() {
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = DateUtils.toStringOfLocalDateTIme(now, "yyyyMMdd");
        OrderSequence orderSequence = orderSequenceRepository.findByDate(yyyyMMdd)
                .orElseGet(() -> OrderSequence.createSequence(now));
        if(orderSequence.isNew()) {
            orderSequenceRepository.save(orderSequence);
        }
        return orderSequence.generateOrderCode();
    }
}
