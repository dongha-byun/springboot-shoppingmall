package springboot.shoppingmall.order.partners.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import springboot.shoppingmall.authorization.configuration.AuthenticationConfig;
import springboot.shoppingmall.order.partners.presentation.response.PartnersCancelOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersDeliveryOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = PartnersOrderQueryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        AuthenticationConfig.class,
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
        when(partnersReadyOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersReadyOrderQueryResponse(
                                1L, 1L, "order-code-1", "2023-07-01", "product-code-1", "상품 1",
                                2, "", 23000, "구매자 1", "010-1234-1234", "상품 준비 중",
                                "수령인 1", "010-1111-2222", "주소 1", "상세주소 1", "요청사항 1"),
                        new PartnersReadyOrderQueryResponse(
                                2L, 2L, "order-code-2", "2023-07-02", "product-code-2", "상품 2",
                                4, "", 34000, "구매자 2", "010-2345-2345", "상품 준비 중",
                                "수령인 2", "010-2222-3333", "주소 2", "상세주소 2", "요청사항 2"),
                        new PartnersReadyOrderQueryResponse(
                                3L, 3L, "order-code-3", "2023-07-03", "product-code-3", "상품 3",
                                8, "", 49900, "구매자 3", "010-3456-3456", "상품 준비 중",
                                "수령인 3", "010-3333-4444", "주소 3", "상세주소 3", "요청사항 3")
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
        when(partnersDeliveryOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersDeliveryOrderQueryResponse(
                                1L, 1L, "order-code-1", "2023-07-01", "product-code-1", "상품 1",
                                2, "", 23000, "구매자 1", "010-1234-1234", "상품 준비 중",
                                "수령인 1", "010-1111-2222", "주소 1", "상세주소 1", "요청사항 1"),
                        new PartnersDeliveryOrderQueryResponse(
                                3L, 3L, "order-code-3", "2023-07-03", "product-code-3", "상품 3",
                                8, "", 49900, "구매자 3", "010-3456-3456", "상품 준비 중",
                                "수령인 3", "010-3333-4444", "주소 3", "상세주소 3", "요청사항 3")
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
        when(partnersEndOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersEndOrderQueryResponse(
                                1L, 1L, "order-code-1", "2023-07-01", "product-code-1", "상품 1",
                                2, "", 23000, "구매자 1", "010-1234-1234", "배송완료",
                                "수령인 1", "010-1111-2222", "주소 1", "상세주소 1", "요청사항 1", "2023-08-11", "문 앞"),
                        new PartnersEndOrderQueryResponse(
                                2L, 2L, "order-code-2", "2023-07-02", "product-code-2", "상품 2",
                                5, "", 33000, "구매자 2", "010-2345-2345", "배송완료",
                                "수령인 2", "010-2222-3333", "주소 2", "상세주소 2", "요청사항 2", "2023-08-12", "택배보관함"),
                        new PartnersEndOrderQueryResponse(
                                3L, 3L, "order-code-3", "2023-07-03", "product-code-3", "상품 3",
                                10, "", 19900, "구매자 3", "010-3456-3456", "배송완료",
                                "수령인 3", "010-3333-4444", "주소 3", "상세주소 3", "요청사항 3", "2023-08-13", "문 앞")
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
        when(partnersCancelOrderQueryService.findPartnersOrders(any(), any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersCancelOrderQueryResponse(
                                1L, 1L, "order-code-1", "2023-07-01", "product-code-1", "상품 1",
                                2, "", 23000, "구매자 1", "010-1234-1234", "주문 취소",
                                "2023-07-22", "단순 변심", "", "", "", ""),
                        new PartnersCancelOrderQueryResponse(
                                2L, 2L, "order-code-2", "2023-08-01", "product-code-2", "상품 2",
                                1, "invoice-number-2", 99000, "구매자 2", "010-2345-2345", "환불요청",
                                "", "", "2023-08-04", "사이즈 안맞음", "", ""),
                        new PartnersCancelOrderQueryResponse(
                                4L, 4L, "order-code-4", "2023-09-03", "product-code-4", "상품 4",
                                1, "invoice-number-4", 79900, "구매자 4", "010-4567-4567", "환불완료",
                                "", "", "2023-09-04", "사이즈 안맞음", "", ""),
                        new PartnersCancelOrderQueryResponse(
                                3L, 3L, "order-code-3", "2023-09-01", "product-code-3", "상품 3",
                                1, "", 69900, "구매자 3", "010-3456-3456", "검수중",
                                "", "", "", "", "2023-09-03", "원하는 색상이 아님")
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