package springboot.shoppingmall.order.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;

@RequiredArgsConstructor
@Component
public class OrderValidator {
    private final OrderFinder orderFinder;

    public void validateOrderIsEnd(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        if(!order.isDeliveryEnd()) {
            throw new IllegalArgumentException("배송 완료된 주문이 아닙니다.");
        }
    }
}
