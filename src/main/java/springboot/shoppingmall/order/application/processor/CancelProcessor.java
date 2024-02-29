package springboot.shoppingmall.order.application.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.handler.OrderCanceledEventHandler;

@RequiredArgsConstructor
@Component
public class CancelProcessor implements ResolutionProcessor {
    private final OrderCanceledEventHandler handler;

    @Override
    public void doProcess(OrderItem orderItem) {
        orderItem.cancel();

        handler.handle(orderItem);
    }
}
