package springboot.shoppingmall.coupon.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class UserCouponQueryRepositoryTest {

    @Autowired
    UserCouponQueryRepository queryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @DisplayName("쿠폰을 발급받은 대상자를 모두 조회한다.")
    @Test
    void find_user_received_coupon() {
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
        List<UserCouponQueryDto> usersReceivedCoupon = queryRepository.findAllUserReceivedCoupon(coupon);

        // then
        assertThat(usersReceivedCoupon).hasSize(3)
                .extracting("userName", "userGrade", "usingDate")
                .containsExactly(
                        tuple("쿠폰발급자1", UserGrade.NORMAL, null),
                        tuple("쿠폰발급자2", UserGrade.NORMAL, null),
                        tuple("쿠폰발급자3", UserGrade.NORMAL, null)
                );
    }
}