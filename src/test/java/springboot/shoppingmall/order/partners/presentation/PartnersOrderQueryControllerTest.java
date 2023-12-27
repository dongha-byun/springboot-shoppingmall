package springboot.shoppingmall.order.partners.presentation;

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
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;
import springboot.shoppingmall.partners.config.PartnersConfiguration;

@WebMvcTest(
        controllers = PartnersOrderQueryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class
                }
        )
)
class PartnersOrderQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PartnersReadyOrderQueryService partnersReadyOrderQueryService;

    @MockBean
    PartnersDeliveryOrderQueryService partnersDeliveryOrderQueryService;

    @MockBean
    PartnersEndOrderQueryService partnersEndOrderQueryService;

    @MockBean
    PartnersCancelOrderQueryService partnersCancelOrderQueryService;

    @Test
    @DisplayName("판매자가 자신의 상품 중에 배송 준비중인 주문 목록을 조회한다.")
    void list_order_of_ready() throws Exception {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2023, 7, 1, 0, 0, 0);
        when(partnersReadyOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersReadyOrderQueryDto(
                                1L, 1L, "order-code-1", orderDate.plusDays(1),
                                "product-code-1", "상품 1", 2, "", 23000,
                                1L, "구매자 1", "010-1234-1234", OrderStatus.PREPARED,
                                "수령인 1", "010-1111-2222",
                                "주소 1", "상세주소 1", "요청사항 1"),
                        new PartnersReadyOrderQueryDto(
                                2L, 2L, "order-code-2", orderDate.plusDays(2),
                                "product-code-2", "상품 2", 4, "", 34000,
                                2L, "구매자 2", "010-2345-2345", OrderStatus.PREPARED,
                                "수령인 2", "010-2222-3333",
                                "주소 2", "상세주소 2", "요청사항 2"),
                        new PartnersReadyOrderQueryDto(
                                3L, 3L, "order-code-3", orderDate.plusDays(3),
                                "product-code-3", "상품 3", 8, "", 49900,
                                3L, "구매자 3", "010-3456-3456", OrderStatus.PREPARED,
                                "수령인 3", "010-3333-4444",
                                "주소 3", "상세주소 3", "요청사항 3")
                )
        );

        // when & then
        mockMvc.perform(
                        get("/partners/orders?"
                                        + "type={type}"
                                        + "&startDate={startDate}"
                                        + "&endDate={endDate}",
                                "READY", "2023-06-11", "2023-09-11")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("판매자가 자신의 상품 중에 배송 중인 주문 목록을 조회한다.")
    void list_order_of_delivery() throws Exception {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2023, 7, 1, 0, 0, 0);
        LocalDateTime deliveryStartDate = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
        when(partnersDeliveryOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersDeliveryOrderQueryDto(
                                1L, 1L, "order-code-1", orderDate.plusDays(1),
                                "product-code-1", "상품 1", 2, "", 23000,
                                1L, "구매자 1", "010-1234-1234",
                                OrderStatus.DELIVERY, deliveryStartDate,
                                "수령인 1", "010-1111-2222",
                                "주소 1", "상세주소 1", "요청사항 1"),
                        new PartnersDeliveryOrderQueryDto(
                                3L, 3L, "order-code-3", orderDate.plusDays(2),
                                "product-code-3", "상품 3", 8, "", 49900,
                                3L, "구매자 3", "010-3456-3456",
                                OrderStatus.DELIVERY, deliveryStartDate,
                                "수령인 3", "010-3333-4444",
                                "주소 3", "상세주소 3", "요청사항 3")
                )
        );

        // when & then
        mockMvc.perform(
                        get("/partners/orders?"
                                        + "type={type}"
                                        + "&startDate={startDate}"
                                        + "&endDate={endDate}",
                                "DELIVERY", "2023-06-11", "2023-09-11")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("판매자가 자신의 상품 중에 배송이 완료된 주문 목록을 조회한다.")
    void list_order_of_end() throws Exception {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2023, 7, 1, 0, 0, 0);
        LocalDateTime deliveryDate = LocalDateTime.of(2023, 7, 3, 0, 0, 0);
        when(partnersEndOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersEndOrderQueryDto(
                                1L, 1L, "order-code-1", orderDate.plusDays(1),
                                "product-code-1", "상품 1", 2, "", 23000,
                                1L, "구매자 1", "010-1234-1234", OrderStatus.DELIVERY_END,
                                "수령인 1", "010-1111-2222",
                                "주소 1", "상세주소 1", "요청사항 1",
                                deliveryDate.plusDays(1), "문 앞"),
                        new PartnersEndOrderQueryDto(
                                2L, 2L, "order-code-2", orderDate.plusDays(2),
                                "product-code-2", "상품 2", 5, "", 33000,
                                2L, "구매자 2", "010-2345-2345", OrderStatus.DELIVERY_END,
                                "수령인 2", "010-2222-3333",
                                "주소 2", "상세주소 2", "요청사항 2",
                                deliveryDate.plusDays(2), "택배보관함"),
                        new PartnersEndOrderQueryDto(
                                3L, 3L, "order-code-3", orderDate.plusDays(3),
                                "product-code-3", "상품 3", 10, "", 19900,
                                3L, "구매자 3", "010-3456-3456", OrderStatus.DELIVERY_END,
                                "수령인 3", "010-3333-4444",
                                "주소 3", "상세주소 3", "요청사항 3",
                                deliveryDate.plusDays(3), "문 앞")
                )
        );

        // when & then
        mockMvc.perform(
                        get("/partners/orders?"
                                        + "type={type}"
                                        + "&startDate={startDate}"
                                        + "&endDate={endDate}",
                                "END", "2023-06-11", "2023-09-11")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("판매자가 자신의 상품 중에 주문취소, 환불, 교환된 목록을 조회한다.")
    void list_order_of_cancel() throws Exception {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2023, 7, 1, 0, 0, 0);
        LocalDateTime cancelDate = LocalDateTime.of(2023, 7, 15, 0, 0, 0);
        when(partnersCancelOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersCancelOrderQueryDto(
                                1L, 1L, "order-code-1", orderDate.plusDays(1),
                                "product-code-1", "상품 1", 2, "", 23000,
                                1L, "구매자 1", "010-1234-1234", OrderStatus.CANCEL,
                                OrderItemResolutionType.CANCEL, cancelDate.plusDays(1), "단순 변심"),
                        new PartnersCancelOrderQueryDto(
                                2L, 2L, "order-code-2", orderDate.plusDays(2),
                                "product-code-2", "상품 2", 1, "invoice-number-2", 99000,
                                2L, "구매자 2", "010-2345-2345", OrderStatus.REFUND,
                                OrderItemResolutionType.REFUND, cancelDate.plusDays(10), "사이즈 안맞음"),
                        new PartnersCancelOrderQueryDto(
                                4L, 4L, "order-code-4", orderDate.plusDays(3),
                                "product-code-4", "상품 4", 1, "invoice-number-4", 79900,
                                4L, "구매자 4", "010-4567-4567", OrderStatus.REFUND_END,
                                OrderItemResolutionType.REFUND, cancelDate.plusDays(15), "사이즈 안맞음"),
                        new PartnersCancelOrderQueryDto(
                                3L, 3L, "order-code-3", orderDate.plusDays(4),
                                "product-code-3", "상품 3", 1, "", 69900,
                                3L, "구매자 3", "010-3456-3456", OrderStatus.EXCHANGE,
                                OrderItemResolutionType.EXCHANGE, cancelDate.plusDays(8), "원하는 색상이 아님")
                )
        );

        // when & then
        mockMvc.perform(
                        get("/partners/orders?"
                                        + "type={type}"
                                        + "&startDate={startDate}"
                                        + "&endDate={endDate}",
                                "CANCEL", "2023-06-11", "2023-09-11")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}