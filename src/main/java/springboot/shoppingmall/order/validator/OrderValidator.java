package springboot.shoppingmall.order.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;

@RequiredArgsConstructor
@Component
public class OrderValidator {
    private final OrderFinder orderFinder;

    public void validateOrderIsEnd(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        if(!orderItem.isDeliveryComplete()) {
            throw new IllegalArgumentException("배송 완료된 주문이 아닙니다.");
        }
    }
}
