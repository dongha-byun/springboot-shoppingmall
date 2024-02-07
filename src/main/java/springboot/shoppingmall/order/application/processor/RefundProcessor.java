package springboot.shoppingmall.order.application.processor;

import org.springframework.stereotype.Component;
import springboot.shoppingmall.order.domain.OrderItem;

@Component
public class RefundProcessor implements ResolutionProcessor {

    @Override
    public void doProcess(OrderItem orderItem) {
        orderItem.refund();
    }
}
