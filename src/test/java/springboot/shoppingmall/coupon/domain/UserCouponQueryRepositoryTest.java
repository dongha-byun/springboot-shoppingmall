package springboot.shoppingmall.coupon.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.application.UsableCouponDto;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserGrade;
import springboot.shoppingmall.userservice.user.domain.UserRepository;

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

    @DisplayName("주문 상품에 적용이 가능한 쿠폰 목록을 조회한다.")
    @Test
    void find_coupons_partners() {
        // given
        User couponReceiver1 = userRepository.save(
                new User("쿠폰발급자1", "coupon_receiver1", "a", "010-2222-3333")
        );
        Coupon coupon1 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #1",
                        new UsingDuration(
                                LocalDateTime.of(2023, 3, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 8, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        Coupon coupon2 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #2",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 15, 1L)
        );
        Coupon coupon3 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #3",
                        new UsingDuration(
                                LocalDateTime.of(2023, 5, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 9, 11, 0, 0, 0)
                        ), 8, 1L)
        );
        coupon1.addUserCoupon(couponReceiver1.getId());
        coupon2.addUserCoupon(couponReceiver1.getId());
        coupon3.addUserCoupon(couponReceiver1.getId());

        // when
        List<UsableCouponDto> usableCouponList = queryRepository.findUsableCouponList(couponReceiver1.getId(), 1L);

        // then
        assertThat(usableCouponList).hasSize(3)
                .extracting("name", "discountRate")
                .containsExactly(
                        tuple("신규 카테고리 오픈 기념 쿠폰 #2", 15),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #1", 10),
                        tuple("신규 카테고리 오픈 기념 쿠폰 #3", 8)
                );
    }

    @DisplayName("상품 주문 시, 이미 사용한 쿠폰은 보이지 않는다.")
    @Test
    void find_usable_coupon_list_without_used() {
        // given
        User couponReceiver1 = userRepository.save(
                new User("쿠폰발급자1", "coupon_receiver1", "a", "010-2222-3333")
        );
        Coupon coupon = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰",
                        new UsingDuration(
                                LocalDateTime.of(2023, 3, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 8, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        Coupon coupon1 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #1",
                        new UsingDuration(
                                LocalDateTime.of(2023, 3, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 8, 31, 0, 0, 0)
                        ), 10, 1L)
        );
        Coupon coupon2 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #2",
                        new UsingDuration(
                                LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 31, 0, 0, 0)
                        ), 15, 1L)
        );
        Coupon coupon3 = couponRepository.save(
                new Coupon("신규 카테고리 오픈 기념 쿠폰 #3",
                        new UsingDuration(
                                LocalDateTime.of(2023, 5, 1, 0, 0, 0),
                                LocalDateTime.of(2023, 9, 11, 0, 0, 0)
                        ), 8, 1L)
        );
        coupon1.addUserCoupon(couponReceiver1.getId());
        coupon2.addUserCoupon(couponReceiver1.getId());
        coupon3.addUserCoupon(couponReceiver1.getId());

        UserCoupon userCoupon1 = coupon1.getUserCoupons().get(0);
        UserCoupon userCoupon2 = coupon2.getUserCoupons().get(0);
        UserCoupon userCoupon3 = coupon3.getUserCoupons().get(0);
        userCoupon2.use();

        // when
        List<UsableCouponDto> usableCouponList = queryRepository.findUsableCouponList(couponReceiver1.getId(), 1L);

        // then
        assertThat(usableCouponList).hasSize(2)
                .extracting("id", "name", "discountRate")
                .containsExactly(
                        tuple(userCoupon1.getId(), "신규 카테고리 오픈 기념 쿠폰 #1", 10),
                        tuple(userCoupon3.getId(), "신규 카테고리 오픈 기념 쿠폰 #3", 8)
                );
    }
}