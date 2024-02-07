package springboot.shoppingmall.order.application;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.application.processor.ResolutionProcessor;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistory;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistoryRepository;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderItemResolutionService {
    private final OrderItemResolutionHistoryRepository repository;
    private final Map<OrderItemResolutionType, ResolutionProcessor> resolutionProcessors;
    private final OrderFinder orderFinder;

    public Long saveResolutionHistory(
            Long userId, Long orderItemId, OrderItemResolutionType resolutionType,
            LocalDateTime dateTime, String content
    ) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);

        OrderItemResolutionHistory resolutionHistory = repository.save(
                new OrderItemResolutionHistory(
                        orderItem, resolutionType, dateTime, content
                )
        );
        ResolutionProcessor resolutionProcessor = resolutionProcessors.get(resolutionType);
        resolutionProcessor.doProcess(orderItem);

        return resolutionHistory.getId();
    }
}
