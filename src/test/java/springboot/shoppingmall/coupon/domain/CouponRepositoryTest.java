package springboot.shoppingmall.coupon.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CouponRepositoryTest {

    @Autowired
    CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 저장")
    void save_test() {
        // given
        Coupon coupon = Coupon.create(
                LocalDateTime.of(2023, 6, 6, 0, 0, 0),
                LocalDateTime.of(2023, 12, 6, 23, 59, 59),
                5, 1L
        );

        // when
        Coupon savedCoupon = couponRepository.save(coupon);

        // then
        assertThat(savedCoupon).isEqualTo(coupon);
    }

    @Test
    @DisplayName("쿠폰코드로 쿠폰 정보 조회")
    void find_id_test() {
        // given
        Coupon coupon = Coupon.create(
                LocalDateTime.of(2023, 6, 6, 0, 0, 0),
                LocalDateTime.of(2023, 12, 6, 23, 59, 59),
                5, 1L
        );
        Coupon savedCoupon = couponRepository.save(coupon);

        // when
        Coupon findCoupon = couponRepository.findById(savedCoupon.getId())
                .orElseThrow();

        // then
        assertThat(findCoupon).isEqualTo(savedCoupon);
    }
}