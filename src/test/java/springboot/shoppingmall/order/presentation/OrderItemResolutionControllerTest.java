package springboot.shoppingmall.order.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.GatewayConstants;
import springboot.shoppingmall.order.application.OrderItemResolutionService;

@WebMvcTest(controllers = OrderItemResolutionController.class)
public class OrderItemResolutionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderItemResolutionService service;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문한 상품의 주문을 취소한다.")
    void save_resolution_history() throws Exception {
        // given
        OrderItemResolutionRequest resolutionRequest = new OrderItemResolutionRequest("주문 취소 합니다.");
        String body = objectMapper.writeValueAsString(resolutionRequest);
        when(service.saveResolutionHistory(any(), any(), any(), any(), any())).thenReturn(
                1L
        );

        // when & then
        mockMvc.perform(post("/orders/item/{orderItemId}/{resolutionType}", 10L, "CANCEL")
                        .header(GatewayConstants.GATEWAY_HEADER, 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderItemId", is(10)))
                .andExpect(jsonPath("$.message", is("주문이 성공적으로 취소되었습니다.")));
    }

    @Test
    @DisplayName("사유를 반드시 입력해야 한다.")
    void save_resolution_history_fail() throws Exception {
        // given
        OrderItemResolutionRequest resolutionRequest = new OrderItemResolutionRequest("");
        String body = objectMapper.writeValueAsString(resolutionRequest);

        // when & then
        mockMvc.perform(post("/orders/item/{orderItemId}/{resolutionType}", 10L, "CANCEL")
                        .header(GatewayConstants.GATEWAY_HEADER, 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("사유는 필수항목 입니다.")));
    }
}
