package springboot.shoppingmall.order.application.processor;

import springboot.shoppingmall.order.domain.OrderItem;

public interface ResolutionProcessor {

    void doProcess(OrderItem orderItem);
}
