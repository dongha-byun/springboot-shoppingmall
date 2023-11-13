package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.application.dto.ResponseUserInformation;
import springboot.shoppingmall.coupon.client.UserCouponService;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.domain.UsingDuration;

@Transactional
@SpringBootTest
class UserCouponQueryServiceTest {

    @Autowired
    UserCouponQueryService queryService;

    @MockBean
    UserCouponService userCouponService;

    @Autowired
    CouponRepository couponRepository;

    @DisplayName("쿠폰 발급 대상자 목록을 조회한다.")
    @Test
    void find_users_received_coupon() {
        // given
        List<Long> userIds = Arrays.asList(100L, 200L, 300L);
        Coupon coupon = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        userIds.forEach(
                coupon::addUserCoupon
        );

        when(userCouponService.getUsers(userIds)).thenReturn(
                Arrays.asList(
                        new ResponseUserInformation(100L, "사용자 100", "일반회원"),
                        new ResponseUserInformation(200L, "사용자 200", "단골회원"),
                        new ResponseUserInformation(300L, "사용자 300", "VVIP")
                )
        );

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
}