package springboot.shoppingmall.orderhistory.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;
import springboot.shoppingmall.userservice.user.domain.User;

public interface OrderHistoryRepository {
    List<OrderHistoryDto> queryOrderHistory(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
