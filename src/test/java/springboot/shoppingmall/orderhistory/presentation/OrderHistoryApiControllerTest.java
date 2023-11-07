package springboot.shoppingmall.orderhistory.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.orderhistory.application.OrderHistoryService;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = OrderHistoryApiController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class
                }
        )
)
class OrderHistoryApiControllerTest {

    @MockBean
    OrderHistoryService orderHistoryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("특정 사용자의 주문내역 목록을 조회한다.")
    void find_order_history_by_user() throws Exception {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2023, 7, 11, 12, 0, 0);
        when(orderHistoryService.findOrderHistory(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new OrderHistoryDto(1L, 1L, orderDate.minusDays(1), OrderStatus.DELIVERY, 100L, "상품 1", "test-tid-1", 23000, 1L, "부실건설"),
                        new OrderHistoryDto(2L, 2L, orderDate, OrderStatus.DELIVERY_END, 200L, "상품 2", "test-tid-2", 43000, 1L, "부실건설"),
                        new OrderHistoryDto(3L, 3L, orderDate.plusDays(2), OrderStatus.CANCEL, 300L, "상품 3", "test-tid-3", 12200, 1L, "부실건설")
                )
        );

        // when & then
        mockMvc.perform(get("/user/orders?startDate={startDate}&endDate={endDate}",
                        "2023-06-22", "2023-09-21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$..orderStatusName", hasItems("배송중", "배송완료", "주문 취소")))
                .andExpect(jsonPath("$..orderDate", hasItems(
                        "2023-07-10 12:00:00",
                        "2023-07-11 12:00:00",
                        "2023-07-13 12:00:00"
                )))
        ;
    }
}