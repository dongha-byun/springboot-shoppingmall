package springboot.shoppingmall.orderhistory.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.orderhistory.domain.OrderHistoryRepository;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    OrderHistoryRepository orderHistoryRepository;

    OrderHistoryService orderHistoryService;

    @Test
    @DisplayName("주문 내역 조회")
    void findOrderHistories(){
        String tid = "test-tid";
        orderHistoryService = new OrderHistoryService(orderHistoryRepository);

        when(orderHistoryRepository.queryOrderHistory(any(), any(), any())).thenReturn(Arrays.asList(
                new OrderHistoryDto(100L, 1L, LocalDateTime.now(), OrderStatus.PREPARED, 1L,"제품1", tid, 23000, 11L, "판매처1"),
                new OrderHistoryDto(101L, 2L, LocalDateTime.now(), OrderStatus.PREPARED, 2L,"제품2", tid, 20000, 11L, "판매처1"),
                new OrderHistoryDto(102L, 3L, LocalDateTime.now(), OrderStatus.PREPARED, 3L,"제품3", tid, 13000, 11L, "판매처1")
        ));

        List<OrderHistoryDto> orderHistory = orderHistoryService.findOrderHistory(1L, LocalDateTime.now().minusMonths(3), LocalDateTime.now());

        assertThat(orderHistory).hasSize(3);
    }

}