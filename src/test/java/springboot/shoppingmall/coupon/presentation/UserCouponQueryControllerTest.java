package springboot.shoppingmall.coupon.presentation;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.coupon.application.UsableCouponDto;
import springboot.shoppingmall.coupon.application.UserCouponQueryService;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;

@WebMvcTest(controllers = UserCouponQueryController.class)
class UserCouponQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserCouponQueryService queryService;

    @DisplayName("쿠폰 발급대상자 목록을 조회한다.")
    @Test
    void find_user_list_received_coupon() throws Exception {
        // given
        when(queryService.findUsersReceivedCoupon(anyLong())).thenReturn(
                Arrays.asList(
                        new UserCouponQueryDto(10L, "10번째 회원", "일반회원", null),
                        new UserCouponQueryDto(20L, "20번째 회원", "단골회원", null),
                        new UserCouponQueryDto(30L, "30번째 회원", "VVIP", null)
                )
        );

        // when & then
        mockMvc.perform(get("/partners/coupons/{id}/users", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("상품 주문 시, 상품에 적용할 수 있는 쿠폰 목록을 조회한다.")
    @Test
    void find_coupons_partners() throws Exception {
        // given
        when(queryService.findUsableCouponList(any(), any())).thenReturn(
                Arrays.asList(
                        new UsableCouponDto(1L, "신규 쿠폰 #1",
                                LocalDateTime.of(2023, 5, 1, 0, 0),
                                LocalDateTime.of(2023, 10, 1, 0, 0),
                                10
                        ),
                        new UsableCouponDto(2L, "신규 쿠폰 #2",
                                LocalDateTime.of(2023, 8, 1, 0, 0),
                                LocalDateTime.of(2023, 11, 1, 0, 0),
                                6
                        ),
                        new UsableCouponDto(3L, "신규 쿠폰 #3",
                                LocalDateTime.of(2023, 3, 1, 0, 0),
                                LocalDateTime.of(2023, 12, 1, 0, 0),
                                8
                        )

                )
        );

        // when & then
        mockMvc.perform(get("/order/coupons?partnersId={partnersId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}