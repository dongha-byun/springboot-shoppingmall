package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.user.domain.OrderHistoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    UserFinder userFinder;

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
        String tid = "test-tid";
        orderHistoryService = new OrderHistoryService(orderHistoryRepository, userFinder);

        when(userFinder.findUserById(any())).thenReturn(user);
        when(orderHistoryRepository.queryOrderHistory(any(), any(), any())).thenReturn(Arrays.asList(
                new OrderHistoryDto(100L, 1L, LocalDateTime.now(), OrderStatus.READY, 1L,"제품1", tid, 23000, 11L, "판매처1"),
                new OrderHistoryDto(101L, 2L, LocalDateTime.now(), OrderStatus.READY, 2L,"제품2", tid, 20000, 11L, "판매처1"),
                new OrderHistoryDto(102L, 3L, LocalDateTime.now(), OrderStatus.READY, 3L,"제품3", tid, 13000, 11L, "판매처1")
        ));

        List<OrderHistoryDto> orderHistory = orderHistoryService.findOrderHistory(user.getId(), LocalDateTime.now().minusMonths(3), LocalDateTime.now());

        assertThat(orderHistory).hasSize(3);
    }

}