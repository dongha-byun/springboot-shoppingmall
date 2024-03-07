package springboot.shoppingmall.order.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.GatewayConstants;
import springboot.shoppingmall.order.application.OrderStatusChangeService;
import springboot.shoppingmall.order.application.dto.ExchangeEndResultDto;
import springboot.shoppingmall.order.application.dto.OrderDeliveryInfoDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderStatus;

@WebMvcTest(controllers = OrderStatusChangeController.class)
class OrderStatusChangeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderStatusChangeService orderStatusChangeService;
    Long orderItemId = 100L;

    @Test
    @DisplayName("판매자가 주문이 들어온 상품을 출고한다.")
    void outing_by_partner() throws Exception {
        // given
        when(orderStatusChangeService.outing(any())).thenReturn(
                new OrderItemDto(
                        orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                        null, null, "", OrderStatus.OUTING
                )
        );

        // when & then
        mockMvc.perform(put("/orders/{orderItemId}/outing", orderItemId)
                        .header(GatewayConstants.GATEWAY_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.orderStatusName", is("상품 출고")));
    }

    @Test
    @DisplayName("구매자가 구매한 상품들을 구매확정처리 한다.")
    void finish_by_user() throws Exception {
        // given
        when(orderStatusChangeService.finish(any())).thenReturn(
                new OrderItemDto(
                        orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                        null, null, "", OrderStatus.FINISH
                )
        );

        // when & then
        mockMvc.perform(put("/orders/{orderItemId}/finish", orderItemId)
                        .header(GatewayConstants.GATEWAY_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.orderStatusName", is("구매 확정")));
    }

    @Test
    @DisplayName("판매자가 교환/환불로 인해 돌려받은 상품을 검수한다.")
    void checking_by_partner() throws Exception {
        // given
        when(orderStatusChangeService.checking(any())).thenReturn(
                new OrderItemDto(
                        orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                        null, null, "", OrderStatus.CHECKING
                )
        );

        // when & then
        mockMvc.perform(put("/orders/{orderItemId}/checking", orderItemId)
                        .header(GatewayConstants.GATEWAY_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.orderStatusName", is("상품 검수중")));
    }

    @Test
    @DisplayName("판매자가 환불된 주문에 대한 환불을 완료한다.")
    void refund_end_by_partner() throws Exception {
        // given
        when(orderStatusChangeService.refundEnd(any())).thenReturn(
                new OrderItemDto(
                        orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                        null, null, "", OrderStatus.REFUND_END
                )
        );

        // when & then
        mockMvc.perform(put("/orders/{orderItemId}/refund-end", orderItemId)
                        .header(GatewayConstants.GATEWAY_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.orderStatusName", is("환불 완료")));
    }

    @Test
    @DisplayName("교환 요청된 상품에 대해 재배송을 진행한다.")
    void exchange_end() throws Exception {
        // given
        when(orderStatusChangeService.exchangeEnd(any())).thenReturn(
                new ExchangeEndResultDto(
                        new OrderItemDto(
                                orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                                null, null, "", OrderStatus.EXCHANGE_END
                        ),
                        new OrderDto(
                                100L, "new-generated-code", 10000L, 11900, 10710,
                                LocalDateTime.now(),
                                List.of(
                                        new OrderItemDto(
                                                orderItemId, 999L, 10, 11900, 10710, null, 0, 1190, "",
                                                null, null, "", OrderStatus.EXCHANGE_END
                                        )
                                ),
                                new OrderDeliveryInfoDto(
                                        "수령인1", "010-2233-4455", "수령인 주소 1", "수령인 상세 주소",
                                        "수령인 주소 우편번호", "문앞에 놔주세요."
                                )
                        )
                )
        );

        // when & then
        mockMvc.perform(post("/orders/{orderItemId}/exchange-end", orderItemId)
                .header(GatewayConstants.GATEWAY_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchangeOrderItem.orderStatusName", is("교환 완료")))
                .andExpect(jsonPath("$.exchangeOrderItem.orderStatusName", is("교환 완료")))
        ;
    }
}