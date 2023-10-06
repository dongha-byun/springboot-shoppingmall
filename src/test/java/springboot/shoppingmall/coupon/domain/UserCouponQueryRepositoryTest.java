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

    @Test
    @DisplayName("특정 쿠폰을 보유한 사용자 ID 정보를 조회한다.")
    void get_user_ids_has_coupon() {
        // given
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 30, 23, 59, 59);
        Coupon coupon = couponRepository.save(
                new Coupon("쿠폰1", new UsingDuration(startDate, endDate), 10, 100L)
        );
        coupon.addUserCoupon(100L);
        coupon.addUserCoupon(200L);
        coupon.addUserCoupon(300L);

        // when
        List<UserCouponDto> userIdsHasCoupon = queryRepository.getUserIdsHasCoupon(coupon.getId());

        // then
        assertThat(userIdsHasCoupon).hasSize(3)
                .extracting("userId", "usingDate")
                .containsAnyOf(
                        tuple(100L, null),
                        tuple(200L, null),
                        tuple(300L, null)
                );
    }
}