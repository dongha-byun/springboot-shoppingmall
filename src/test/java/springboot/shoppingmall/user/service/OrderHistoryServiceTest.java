package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.user.domain.OrderHistoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    OrderHistoryRepository orderHistoryRepository;

    OrderHistoryService orderHistoryService;
    User user;
    @BeforeEach
    void setup(){
        user = User.builder()
                .userName("사용자")
                .loginId("user")
                .password("user1!")
                .telNo("010-1234-1234")
                .build();
    }

    @Test
    @DisplayName("주문 내역 조회")
    void findOrderHistories(){
        orderHistoryService = new OrderHistoryService(orderHistoryRepository, userRepository);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(orderHistoryRepository.queryOrderHistory(any())).thenReturn(Arrays.asList(
                new OrderHistoryDto(100L, LocalDateTime.now(), OrderStatus.READY, "제품1", 23000),
                new OrderHistoryDto(101L, LocalDateTime.now(), OrderStatus.READY, "제품2", 20000),
                new OrderHistoryDto(102L, LocalDateTime.now(), OrderStatus.READY, "제품3", 13000)
        ));

        List<OrderHistoryDto> orderHistory = orderHistoryService.findOrderHistory(user.getId());

        assertThat(orderHistory).hasSize(3);
    }

}