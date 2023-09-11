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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.configuration.AuthenticationConfig;
import springboot.shoppingmall.coupon.application.CouponQueryDto;
import springboot.shoppingmall.coupon.application.CouponQueryService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = CouponQueryController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                PartnersConfiguration.class,
                                AuthenticationConfig.class
                        }
                )
        }
)
class CouponQueryControllerTest {

    @MockBean
    CouponQueryService queryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("판매자가 등록한 쿠폰 목록을 조회한다.")
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

        // when & then
        mockMvc.perform(get("/partners/coupons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", hasSize(2)));
    }
}