package springboot.shoppingmall.user.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.user.dto.OrderHistoryDto;
import springboot.shoppingmall.userservice.user.domain.User;

public interface OrderHistoryRepository {
    List<OrderHistoryDto> queryOrderHistory(User user, LocalDateTime startDate, LocalDateTime endDate);
}
