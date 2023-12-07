package springboot.shoppingmall.order.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private final OrderFinder orderFinder;

    public Long saveResolutionHistory(
            Long userId, Long orderItemId, OrderItemResolutionType resolutionType,
            LocalDateTime dateTime, String content
    ) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);

        switch (resolutionType) {
            case CANCEL:
                orderItem.cancel();
                break;
            case REFUND:
                orderItem.refund();
                break;
            case EXCHANGE:
                orderItem.exchange();
                break;
        }

        OrderItemResolutionHistory resolutionHistory = repository.save(
                new OrderItemResolutionHistory(
                        orderItem, resolutionType, dateTime, content
                )
        );

        return resolutionHistory.getId();
    }
}
