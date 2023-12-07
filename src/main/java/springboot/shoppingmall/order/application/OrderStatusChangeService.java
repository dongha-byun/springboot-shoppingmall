package springboot.shoppingmall.order.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStatusChangeService {
    private final OrderFinder orderFinder;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final OrderUserInterfaceService orderUserInterfaceService;
    private final PayHistoryRepository payHistoryRepository;

    // 주문 취소
    public OrderItemDto cancel(Long orderId, Long orderItemId, LocalDateTime cancelDate, String cancelReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.cancel();

        // 주문이 취소되면, 결제취소 요청을 보낸다.
//        PayHistory payHistory = payHistoryRepository.findByOrderId(orderId)
//                .orElseThrow(
//                        () -> new IllegalArgumentException("결제 고유번호 조회 실패")
//                );
//        orderPayService.cancel(payHistory.getTid(), payHistory.getAmount());

        return OrderItemDto.of(orderItem);
    }

    // 출고 중
    public OrderItemDto outing(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(order);
        orderItem.outing(deliveryInvoice.getInvoiceNumber());

        return OrderItemDto.of(orderItem);
    }

    // 구매확정
    public OrderItemDto finish(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.finish();

        // 구매확정 시, 사용자의 주문정보를 업데이트한다.
        orderUserInterfaceService.increaseOrderAmounts(order.getUserId(), orderItem.totalPrice());

        return OrderItemDto.of(orderItem);
    }

    // 검수중
    public OrderItemDto checking(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.checking();

        return OrderItemDto.of(orderItem);
    }

    // 환불
    public OrderItemDto refund(Long orderId, Long orderItemId, LocalDateTime refundDate, String refundReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refund();

        return OrderItemDto.of(orderItem);
    }

    // 환불 완료
    public OrderItemDto refundEnd(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refundEnd();

        return OrderItemDto.of(orderItem);
    }

    // 교환
    public OrderItemDto exchange(Long orderId, Long orderItemId, LocalDateTime exchangeDate, String exchangeReason) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.exchange();

        return OrderItemDto.of(orderItem);
    }
}
