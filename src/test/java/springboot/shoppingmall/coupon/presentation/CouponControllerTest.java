package springboot.shoppingmall.coupon.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.coupon.application.CouponService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartnerArgumentResolver;

@WebMvcTest(controllers = CouponController.class)
class CouponControllerTest {

    @MockBean
    LoginPartnerArgumentResolver loginPartnerArgumentResolver;

    @MockBean
    CouponService service;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("쿠폰 명은 필수 입력값이다.")
    @Test
    void create_coupon_fail_with_no_name() throws Exception {
        // given
        CouponCreateRequest createRequest = CouponCreateRequest.builder()
                .name("")
                .fromDate("")
                .toDate("2023-12-19")
                .userGrade("NORMAL")
                .discountRate(10)
                .build();
        String content = objectMapper.writeValueAsString(createRequest);

        when(loginPartnerArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(loginPartnerArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
                new AuthorizedPartner(1L)
        );

        // when & then
        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].message", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "쿠폰명은 필수 항목 입니다.",
                        "유효기간 시작일은 필수 항목 입니다."
                )));
    }
}