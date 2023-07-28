package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.domain.UsingDuration;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class UserCouponQueryServiceTest {

    @Autowired
    UserCouponQueryService queryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @DisplayName("쿠폰 발급 대상자 목록을 조회한다.")
    @Test
    void find_users_received_coupon() {
        // given
        User couponReceiver1 = userRepository.save(
                new User("쿠폰발급자1", "coupon_receiver1", "a", "010-2222-3333")
        );
        User couponReceiver2 = userRepository.save(
                new User("쿠폰발급자2", "coupon_receiver2", "a", "010-3333-3333")
        );
        User couponReceiver3 = userRepository.save(
                new User("쿠폰발급자3", "coupon_receiver3", "a", "010-4444-3333")
        );
        Coupon coupon = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        coupon.addUserCoupon(couponReceiver1.getId());
        coupon.addUserCoupon(couponReceiver2.getId());
        coupon.addUserCoupon(couponReceiver3.getId());

        // when
        List<UserCouponQueryDto> usersReceivedCoupon = queryService.findUsersReceivedCoupon(coupon.getId());

        // then
        assertThat(usersReceivedCoupon).hasSize(3)
                .extracting("userName", "userGrade", "usingDate")
                .containsExactly(
                        tuple("쿠폰발급자1", UserGrade.NORMAL, null),
                        tuple("쿠폰발급자2", UserGrade.NORMAL, null),
                        tuple("쿠폰발급자3", UserGrade.NORMAL, null)
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
        List<CouponQueryDto> coupons = queryService.findUsableCouponList(10L, 1L);

        // then
        assertThat(coupons).hasSize(3)
                .extracting("name", "discountRate")
                .containsExactly(
                        tuple("신규 카테고리 오픈 기념 쿠폰 #2", 10),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #3", 8),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #1", 7)
                );
    }
}