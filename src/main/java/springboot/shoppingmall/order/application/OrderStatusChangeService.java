package springboot.shoppingmall.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.application.dto.OrderItemDto;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStatusChangeService {
    private final OrderFinder orderFinder;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final OrderUserInterfaceService orderUserInterfaceService;

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

    // 환불 완료
    public OrderItemDto refundEnd(Long orderId, Long orderItemId) {
        Order order = orderFinder.findOrderById(orderId);
        OrderItem orderItem = order.findOrderItem(orderItemId);
        orderItem.refundEnd();

        return OrderItemDto.of(orderItem);
    }
}
