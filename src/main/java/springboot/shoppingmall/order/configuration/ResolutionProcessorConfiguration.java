package springboot.shoppingmall.order.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.application.processor.CancelProcessor;
import springboot.shoppingmall.order.application.processor.ExchangeProcessor;
import springboot.shoppingmall.order.application.processor.RefundProcessor;
import springboot.shoppingmall.order.application.processor.ResolutionProcessor;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;

@RequiredArgsConstructor
@Configuration
public class ResolutionProcessorConfiguration {
    private final CancelProcessor cancelProcessor;
    private final RefundProcessor refundProcessor;
    private final ExchangeProcessor exchangeProcessor;

    @Bean
    public Map<OrderItemResolutionType, ResolutionProcessor> resolutionProcessors() {
        Map<OrderItemResolutionType, ResolutionProcessor> map = new HashMap<>();
        map.put(OrderItemResolutionType.CANCEL, cancelProcessor);
        map.put(OrderItemResolutionType.REFUND, refundProcessor);
        map.put(OrderItemResolutionType.EXCHANGE, exchangeProcessor);

        return map;
    }
}
