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

    // 출고 중 - 파트너
    public OrderItemDto outing(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(orderItem.getOrder());
        orderItem.outing(deliveryInvoice.getInvoiceNumber());

        return OrderItemDto.of(orderItem);
    }

    // 구매확정 - 사용자
    public OrderItemDto finish(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.finish();

        // 구매확정 시, 사용자의 주문정보를 업데이트한다.
        Order order = orderItem.getOrder();
        orderUserInterfaceService.increaseOrderAmounts(order.getUserId(), orderItem.totalPrice());

        return OrderItemDto.of(orderItem);
    }

    // 검수중 - 파트너
    public OrderItemDto checking(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.checking();

        return OrderItemDto.of(orderItem);
    }

    // 환불 완료 - 파트너
    public OrderItemDto refundEnd(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.refundEnd();

        return OrderItemDto.of(orderItem);
    }
}
