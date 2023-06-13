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

    private final OrderFinder orderFinder;
    private final ProductFinder productFinder;
    private final UserFinder userFinder;
    private final OrderRepository orderRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final OrderPayService orderPayService;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        List<OrderItem> items = getOrderItems(orderRequest);

        String orderCode = generateOrderCode();
        Order newOrder = orderRepository.save(
                Order.createOrder(orderCode, userId, items,
                        orderRequest.getReceiverName(), orderRequest.getReceiverPhoneNumber(),
                        orderRequest.getZipCode(), orderRequest.getAddress(), orderRequest.getDetailAddress(),
                        orderRequest.getRequestMessage())
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

    // 주문 취소
    @Transactional
    public OrderItemResponse cancel(Long orderId, Long orderItemId, LocalDateTime cancelDate, String cancelReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.cancel(cancelDate, cancelReason);

        // 주문이 취소되면, 결제취소 요청을 보낸다.
//        PayHistory payHistory = payHistoryRepository.findByOrderId(orderId)
//                .orElseThrow(
//                        () -> new IllegalArgumentException("결제 고유번호 조회 실패")
//                );
//        orderPayService.cancel(payHistory.getTid(), payHistory.getAmount());

        return OrderItemResponse.of(orderItem);
    }

    // 출고 중
    @Transactional
    public OrderItemResponse outing(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(order);
        orderItem.outing(deliveryInvoice.getInvoiceNumber());

        return OrderItemResponse.of(orderItem);
    }

    // 구매확정
    @Transactional
    public OrderItemResponse finish(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.finish();

        // 구매확정 시, 사용자의 주문정보를 업데이트한다.
        User orderUser = userFinder.findUserById(order.getUserId());
        orderUser.increaseOrderAmount(orderItem.totalPrice());

        return OrderItemResponse.of(orderItem);
    }

    // 검수중
    @Transactional
    public OrderItemResponse checking(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.checking();

        return OrderItemResponse.of(orderItem);
    }

    // 환불
    @Transactional
    public OrderItemResponse refund(Long orderId, Long orderItemId, LocalDateTime refundDate, String refundReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refund(refundDate, refundReason);

        return OrderItemResponse.of(orderItem);
    }

    // 환불 완료
    @Transactional
    public OrderItemResponse refundEnd(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refundEnd();

        return OrderItemResponse.of(orderItem);
    }

    // 교환
    @Transactional
    public OrderItemResponse exchange(Long orderId, Long orderItemId, LocalDateTime exchangeDate, String exchangeReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.exchange(exchangeDate, exchangeReason);

        return OrderItemResponse.of(orderItem);
    }
}
