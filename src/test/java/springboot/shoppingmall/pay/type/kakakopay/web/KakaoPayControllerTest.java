package springboot.shoppingmall.pay.type.kakakopay.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.service.KakaoPayService;
import springboot.shoppingmall.pay.type.kakakopay.web.approve.KakaoPayApproveRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.approve.KakaoPayApproveResponse;
import springboot.shoppingmall.pay.type.kakakopay.value.ApprovedCancelAmount;
import springboot.shoppingmall.pay.type.kakakopay.value.CancelAvailableAmount;
import springboot.shoppingmall.pay.type.kakakopay.value.CanceledAmount;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.type.kakakopay.value.Amount;
import springboot.shoppingmall.pay.type.kakakopay.web.ready.KakaoPayReadyRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.ready.KakaoPayReadyResponse;
import springboot.shoppingmall.pay.web.PayRequest;
import springboot.shoppingmall.payment.domain.PayType;

@WebMvcTest(KakaoPayController.class)
class KakaoPayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    KakaoPayService kakaoPayService;

    @Test
    @DisplayName("카카오페이 결제준비 API 를 호출한다.")
    void kakao_pay_ready_api() throws Exception {
        // given
        KakaoPayReadyRequest kakaoPayReadyRequest = new KakaoPayReadyRequest(
                "partner_order_id", "partner_user_id", "상품 1 외 2건",
                2, 15900, 0, 0,
                "approve_url", "fail_url", "cancel_url"
        );

        PayRequest<KakaoPayReadyRequest> kakaoPayReadyRequestPayRequest = new PayRequest<>(
                PayType.KAKAO_PAY.name(), kakaoPayReadyRequest
        );

        String requestBody = objectMapper.writeValueAsString(kakaoPayReadyRequestPayRequest);

        when(kakaoPayService.ready(any())).thenReturn(
                new KakaoPayReadyResponseDto(
                        "test-transaction-id",
                        "next_redirect_app_url",
                        "next_redirect_mobile_url",
                        "next_redirect_pc_url",
                        "android_app_scheme",
                        "ios_app_scheme",
                        LocalDateTime.of(2023, 11, 29, 0, 0, 0)
                )
        );

        // when & then
        mockMvc.perform(post("/pay/KAKAO_PAY/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tid", notNullValue()));
    }

    @Test
    @DisplayName("카카오페이 결제승인 API 를 호출한다.")
    void kakao_pay_approve_api() throws Exception {
        // given
        KakaoPayApproveRequest kakaoPayApproveRequest = new KakaoPayApproveRequest(
                "test-transaction-id",
                "partner_order_id", "partner_user_id",
                "pg_token"
        );

        PayRequest<KakaoPayApproveRequest> kakaoPayApproveRequestPayRequest = new PayRequest<>(
                PayType.KAKAO_PAY.name(), kakaoPayApproveRequest
        );

        String requestBody = objectMapper.writeValueAsString(kakaoPayApproveRequestPayRequest);

        when(kakaoPayService.approve(any())).thenReturn(
                new KakaoPayApproveResponseDto(
                        "test-cid",
                        "test-aid",
                        "test-transaction-id",
                        "partner_user_id",
                        "partner_order_id",
                        "MONEY",
                        "상품 1 외 2건",
                        2,
                        new Amount(
                                15000, 0, 0,
                                0, 0, 0
                        ),
                        LocalDateTime.of(2023, 11, 29, 0, 0, 0),
                        LocalDateTime.of(2023, 11, 29, 0, 0, 0)

                )
        );

        // when & then
        mockMvc.perform(post("/pay/KAKAO_PAY/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tid", notNullValue()))
                .andExpect(jsonPath("$.aid", notNullValue()));
    }

    @Test
    @DisplayName("카카오페이 결제취소 API 를 호출한다.")
    void kakao_pay_cancel_api() throws Exception {
        // given
        KakaoPayCancelRequest kakaoPayCancelRequest = new KakaoPayCancelRequest(
                "test-transaction-id",
                12000, 0
        );

        PayRequest<KakaoPayCancelRequest> kakaoPayCancelRequestPayRequest = new PayRequest<>(
                PayType.KAKAO_PAY.name(), kakaoPayCancelRequest
        );

        String requestBody = objectMapper.writeValueAsString(kakaoPayCancelRequestPayRequest);

        when(kakaoPayService.cancel(any())).thenReturn(
                new KakaoPayCancelResponseDto(
                        "test-aid",
                        "test-transaction-id",
                        "test-cid",
                        "SUCCESS_PAYMENT",
                        "partner_user_id",
                        "partner_order_id",
                        "MONEY",
                        new Amount(),
                        new ApprovedCancelAmount(),
                        new CanceledAmount(),
                        new CancelAvailableAmount(),
                        "상품 1 외 2건",
                        "TEST_PRODUCT_CODE",
                        2,
                        LocalDateTime.of(2023, 11, 29, 0, 0, 0),
                        LocalDateTime.of(2023, 11, 29, 0, 0, 0),
                        LocalDateTime.of(2023, 12, 2, 1, 0, 0)
                )
        );

        // when & then
        mockMvc.perform(post("/pay/KAKAO_PAY/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tid", notNullValue()))
                .andExpect(jsonPath("$.aid", notNullValue()));
    }
}