package springboot.shoppingmall.coupon.presentation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import springboot.shoppingmall.coupon.application.CouponService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = CouponController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                PartnersConfiguration.class
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

    @Test
    @DisplayName("쿠폰 명, 유효기간, 지급대상, 할인율을 모두 입력하면, 쿠폰을 생성할 수 있다.")
    void create_coupon() throws Exception {
        // given
        CouponCreateRequest createRequest = CouponCreateRequest.builder()
                .name("1주년 기념 감사쿠폰")
                .fromDate("2023-06-01")
                .toDate("2023-12-31")
                .userGrade("NORMAL")
                .discountRate(5)
                .build();
        String content = objectMapper.writeValueAsString(createRequest);

        when(couponService.create(any())).thenReturn(10L);

        // when & then
        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponId", is(10)))
                .andExpect(jsonPath("$.message", is("쿠폰이 정상적으로 등록되었습니다.")));
    }
}