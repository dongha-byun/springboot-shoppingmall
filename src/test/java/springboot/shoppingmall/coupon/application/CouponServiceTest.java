package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.domain.UserGradeInfo;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    CouponService couponService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @BeforeEach
    void beforeEach() {
        사용자_추가("쿠폰테스터1", "coupon_tester1", "010-1111-2222", UserGrade.REGULAR);
        사용자_추가("쿠폰테스터2", "coupon_tester2", "010-2222-3333", UserGrade.VIP);
        사용자_추가("쿠폰테스터3", "coupon_tester3", "010-3333-4444", UserGrade.VVIP);
        사용자_추가("쿠폰테스터4", "coupon_tester4", "010-3333-4444", UserGrade.NORMAL);
    }

    private void 사용자_추가(String name, String loginId, String telNo, UserGrade userGrade) {
        userRepository.save(
                new User(
                        name, loginId, "coupon_tester_1!", telNo,
                        0, false,
                        new UserGradeInfo(userGrade, userGrade.getMinOrderCondition(), userGrade.getMinAmountCondition()
                        )
                )
        );
    }

    @Test
    @DisplayName("쿠폰 생성 - 특정 회원등급 이상의 회원들에게 쿠폰을 발급한다.")
    void create_coupon_for_user() {
        // 단골회원(REGULAR) 등급 이상인 회원들에게
        // 사용기한이 2023-05-28 ~ 2023-07-28 인
        // 할인율 7%의 쿠폰을 발급해준다.
        // given
        LocalDateTime fromDate = LocalDateTime.of(2023, 5, 28, 0, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2023, 7, 28, 23, 59, 59);
        int discountRate = 5;
        Long partnersId = 1L;
        CouponCreateDto couponCreateDto = new CouponCreateDto(
                fromDate, toDate, UserGrade.REGULAR, discountRate, partnersId
        );

        // when
        Long couponId = couponService.create(couponCreateDto);

        // then
        Coupon savedCoupon = couponRepository.findById(couponId).orElseThrow();
        assertThat(savedCoupon).isNotNull();
        assertThat(savedCoupon.getId()).isEqualTo(couponId);
        assertThat(savedCoupon.getUserCoupons()).hasSize(3);
    }
}