package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.coupon.application.dto.ResponseUserInformation;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.domain.UsingDuration;

@Transactional
@SpringBootTest
class UserCouponQueryServiceTest {

    @Autowired
    UserCouponQueryService queryService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    RestTemplate restTemplate;

    MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("쿠폰 발급 대상자 목록을 조회한다.")
    @Test
    void find_users_received_coupon() throws JsonProcessingException {
        // given
        Coupon coupon = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        coupon.addUserCoupon(100L);
        coupon.addUserCoupon(200L);
        coupon.addUserCoupon(300L);
        mockingGetUsersHasCoupon();

        // when
        List<UserCouponQueryDto> usersReceivedCoupon = queryService.findUsersReceivedCoupon(coupon.getId());

        // then
        assertThat(usersReceivedCoupon).hasSize(3)
                .extracting("userId", "userName", "userGrade", "usingDate")
                .containsExactly(
                        tuple(100L, "사용자 100", "일반회원", null),
                        tuple(200L, "사용자 200", "단골회원", null),
                        tuple(300L, "사용자 300", "VVIP", null)
                );
    }

    @DisplayName("상품에 적용이 가능한 쿠폰을 조회한다.")
    @Test
    void find_coupons_of_partners() {
        // given
        Coupon coupon1 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #1",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 10, 31, 0, 0, 0)
                        ), 7, 1L)
        );
        Coupon coupon2 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #2",
                        new UsingDuration(
                                LocalDateTime.of(2023, 8, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        Coupon coupon3 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #3",
                        new UsingDuration(
                                LocalDateTime.of(2023, 4, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 9, 22, 0, 0, 0)
                        ), 8, 1L)
        );
        coupon1.addUserCoupon(10L);
        coupon2.addUserCoupon(10L);
        coupon3.addUserCoupon(10L);

        // when
        List<UsableCouponDto> usableCouponList = queryService.findUsableCouponList(10L, 1L);

        // then
        assertThat(usableCouponList).hasSize(3)
                .extracting("name", "discountRate")
                .containsExactly(
                        tuple("신규 카테고리 오픈 기념 쿠폰 #2", 10),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #3", 8),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #1", 7)
                );
    }

    private void mockingGetUsersHasCoupon() throws JsonProcessingException {
        List<ResponseUserInformation> responseUserInformationList = Arrays.asList(
                new ResponseUserInformation(100L, "사용자 100", "일반회원"),
                new ResponseUserInformation(200L, "사용자 200", "단골회원"),
                new ResponseUserInformation(300L, "사용자 300", "VVIP")
        );
        String body = objectMapper.writeValueAsString(responseUserInformationList);
        mockRestServiceServer
                .expect(
                        requestTo("/users/has-coupon")
                )
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body)
                );
    }
}