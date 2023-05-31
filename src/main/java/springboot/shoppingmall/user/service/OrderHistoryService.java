package springboot.shoppingmall.user.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.OrderHistoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final UserFinder userFinder;

    public List<OrderHistoryDto> findOrderHistory(Long userId, LocalDateTime startDate, LocalDateTime endDate){
        User user = userFinder.findUserById(userId);
        return orderHistoryRepository.queryOrderHistory(user, startDate, endDate);
    }
}
