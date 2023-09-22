package springboot.shoppingmall.orderhistory.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;

public interface OrderHistoryRepository {
    List<OrderHistoryDto> queryOrderHistory(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
