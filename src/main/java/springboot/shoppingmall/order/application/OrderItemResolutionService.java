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

    public void process(Long userId, Long orderItemId, OrderItemResolutionType resolutionType, String content) {
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

        if(!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("사유가 입력되지 않았습니다.");
        }

        repository.save(
                new OrderItemResolutionHistory(
                        orderItem, resolutionType, LocalDateTime.now(), content
                )
        );
    }
}
