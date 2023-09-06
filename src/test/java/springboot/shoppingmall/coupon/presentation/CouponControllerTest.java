package springboot.shoppingmall.coupon.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import springboot.shoppingmall.coupon.application.CouponService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = CouponController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                PartnersConfiguration.class, AuthenticationConfig.class
                        }
                )
        }
)
class CouponControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    @DisplayName("쿠폰 명, 유효기간, 지급대상, 할인율 모두 필수로 입력해야 한다.")
    @Test
    void create_coupon_fail_with_no_name() throws Exception {
        // given
        CouponCreateRequest createRequest = CouponCreateRequest.builder().build();
        String content = objectMapper.writeValueAsString(createRequest);

        // when & then
        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "쿠폰명은 필수 항목 입니다.",
                        "유효기간 시작일은 필수 항목 입니다.",
                        "유효기간 종료일은 필수 항목 입니다.",
                        "지급대상은 필수 항목 입니다.",
                        "1% 이상 할인율을 책정해야 합니다."
                )));
    }
}