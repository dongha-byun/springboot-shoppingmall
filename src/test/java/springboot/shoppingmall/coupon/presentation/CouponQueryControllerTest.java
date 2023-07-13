package springboot.shoppingmall.coupon.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.coupon.application.CouponQueryDto;
import springboot.shoppingmall.coupon.application.CouponQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartnerArgumentResolver;

@WebMvcTest(controllers = CouponQueryController.class)
class CouponQueryControllerTest {

    @MockBean
    CouponQueryService queryService;

    @MockBean
    LoginPartnerArgumentResolver loginPartnerArgumentResolver;

    @MockBean
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("판매자가 등록한 쿠폰 목록을 조회한다.")
    @Test
    void find_coupon_all_by_partners() throws Exception {
        // given
        when(queryService.findCouponAll(anyLong())).thenReturn(
                Arrays.asList(
                        new CouponQueryDto(
                                1L, "쿠폰1",
                                LocalDateTime.of(2023,7,7,0,0,0),
                                LocalDateTime.of(2023,7,31,0,0,0),
                                5
                        ),
                        new CouponQueryDto(
                                2L, "쿠폰2",
                                LocalDateTime.of(2023,7,15,0,0,0),
                                LocalDateTime.of(2023,7,31,0,0,0),
                                10
                        )
                )
        );

        when(loginPartnerArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(loginPartnerArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
                new AuthorizedPartner(1L)
        );

        // when & then
        mockMvc.perform(get("/coupons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}