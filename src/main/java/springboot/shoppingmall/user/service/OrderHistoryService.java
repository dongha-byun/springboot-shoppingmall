package springboot.shoppingmall.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.OrderHistoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final UserRepository userRepository;

    public List<OrderHistoryDto> findOrderHistory(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        return orderHistoryRepository.queryOrderHistory(user);
    }
}
