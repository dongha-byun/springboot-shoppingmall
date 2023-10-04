package springboot.shoppingmall.order.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.configuration.AuthenticationConfig;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.DeliveryInfoRequest;
import springboot.shoppingmall.order.dto.OrderItemRequest;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.application.OrderService;
import springboot.shoppingmall.order.application.dto.OrderDeliveryInfoDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = OrderApiController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class,
                        AuthenticationConfig.class
                })
)
class OrderApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("상품을 주문한다.")
    void create_order() throws Exception {
        // given
        List<OrderItemRequest> items = Arrays.asList(
                new OrderItemRequest(1L, 2, null),
                new OrderItemRequest(2L, 1, null),
                new OrderItemRequest(3L, 10, null)
        );
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "김수령인", "010-1234-1234",
                "10999", "수령지 주소", "수령지 상세주소",
                "요청내용 입니다."
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-transaction-id", "KAKAO_PAY", items,
                0, deliveryInfoRequest
        );
        String content = objectMapper.writeValueAsString(orderRequest);

        when(orderService.createOrder(any(), any())).thenReturn(
                new OrderDto(
                        10L, "order-code-10",
                        1L, 39000, 35100, LocalDateTime.of(2023, 9, 12, 1, 30, 0),
                        Arrays.asList(
                                new OrderItemDto(1L ,1L, 2, 20000, 18000, null, 0, 2000, null, null, null, "",
                                        OrderStatus.READY, null, "", null, "", null, ""),
                                new OrderItemDto(2L ,2L, 1, 9000, 8100, null, 0, 900, null, null, null, "",
                                        OrderStatus.READY, null, "", null, "", null, ""),
                                new OrderItemDto(3L ,3L, 10, 10000, 9000, null, 0, 1000, null, null, null, "",
                                        OrderStatus.READY, null, "", null, "", null, "")

                        ),
                        new OrderDeliveryInfoDto(
                                "김수령인", "010-1234-1234",
                                "10999", "수령지 주소", "수령지 상세주소",
                                "요청내용 입니다."
                        )
                )
        );

        // when & then
        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.items", hasSize(3)));
    }
}