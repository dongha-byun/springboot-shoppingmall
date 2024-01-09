package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;
import springboot.shoppingmall.coupon.domain.UsingDuration;

@Transactional
@SpringBootTest
class UserCouponServiceTest {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    UserCouponService userCouponService;

    @Test
    @DisplayName("회원이 이미 사용한 쿠폰을 사용 전 상태로 되돌린다.")
    void recovery_coupon() {
        // given
        Coupon coupon = couponRepository.save(
                new Coupon("오픈 기념 쿠폰 #1",
                        new UsingDuration(
                                LocalDateTime.of(2023, 11, 15, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 23, 59, 59)
                        ),
                        5, 100L
                )
        );
        UserCoupon userCoupon = userCouponRepository.save(
                new UserCoupon(10L, coupon, LocalDateTime.of(2023, 12, 11, 0, 0, 0))
        );
        assertThat(userCoupon.isUsed()).isTrue();

        // when
        userCouponService.recoveryCoupon(userCoupon.getId());

        // then
        assertThat(userCoupon.isUsed()).isFalse();
    }
}