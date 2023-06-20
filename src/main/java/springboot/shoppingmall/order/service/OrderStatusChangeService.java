package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStatusChangeService {
    private final UserFinder userFinder;
    private final OrderFinder orderFinder;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final PayHistoryRepository payHistoryRepository;

    // 주문 취소
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
    public OrderItemResponse outing(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(order);
        orderItem.outing(deliveryInvoice.getInvoiceNumber());

        return OrderItemResponse.of(orderItem);
    }

    // 구매확정
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
    public OrderItemResponse checking(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.checking();

        return OrderItemResponse.of(orderItem);
    }

    // 환불
    public OrderItemResponse refund(Long orderId, Long orderItemId, LocalDateTime refundDate, String refundReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refund(refundDate, refundReason);

        return OrderItemResponse.of(orderItem);
    }

    // 환불 완료
    public OrderItemResponse refundEnd(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refundEnd();

        return OrderItemResponse.of(orderItem);
    }

    // 교환
    public OrderItemResponse exchange(Long orderId, Long orderItemId, LocalDateTime exchangeDate, String exchangeReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.exchange(exchangeDate, exchangeReason);

        return OrderItemResponse.of(orderItem);
    }
}
