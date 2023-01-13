package springboot.shoppingmall.user.domain;

import java.util.List;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

public interface OrderHistoryRepository {
    List<OrderHistoryDto> queryOrderHistory(User user);
}
