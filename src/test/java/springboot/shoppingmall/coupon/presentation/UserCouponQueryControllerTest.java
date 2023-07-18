package springboot.shoppingmall.coupon.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.coupon.application.UserCouponQueryService;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartnerArgumentResolver;
import springboot.shoppingmall.user.domain.UserGrade;

@WebMvcTest(controllers = UserCouponQueryController.class)
class UserCouponQueryControllerTest {

    @MockBean
    LoginPartnerArgumentResolver loginPartnerArgumentResolver;

    @MockBean
    AuthService authService;

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
                        new UserCouponQueryDto(10L, "10번째 회원", UserGrade.NORMAL, null),
                        new UserCouponQueryDto(20L, "20번째 회원", UserGrade.REGULAR, null),
                        new UserCouponQueryDto(30L, "30번째 회원", UserGrade.NORMAL, null)

                )
        );

        when(loginPartnerArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(loginPartnerArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
                new AuthorizedPartner(1L)
        );

        // when & then
        mockMvc.perform(get("/coupons/{id}/users", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}