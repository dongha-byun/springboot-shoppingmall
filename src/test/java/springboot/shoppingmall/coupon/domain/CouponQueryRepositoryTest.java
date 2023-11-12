package springboot.shoppingmall.coupon.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.application.CouponQueryDto;

@Transactional
@SpringBootTest
class CouponQueryRepositoryTest {

    @Autowired
    CouponQueryRepository queryRepository;

    @Autowired
    CouponRepository couponRepository;

    @DisplayName("특정 판매처에서 등록한 쿠폰목록을 조회한다.")
    @Test
    void find_coupon_all_by_partnersId_with_dto() {
        // given
        Long partnerId = 1L;
        Coupon coupon1 = couponRepository.save(createCoupon("쿠폰1", partnerId));
        Coupon coupon2 = couponRepository.save(createCoupon("쿠폰2", partnerId));
        Coupon coupon3 = couponRepository.save(createCoupon("쿠폰3", partnerId));
        Coupon coupon4 = couponRepository.save(createCoupon("쿠폰4", partnerId));

        // when
        List<CouponQueryDto> couponAll = queryRepository.findCouponAll(partnerId);

        // then
        assertThat(couponAll).hasSize(4)
                .extracting("id", "name")
                .containsExactly(
                        tuple(coupon4.getId(), "쿠폰4"),
                        tuple(coupon3.getId(), "쿠폰3"),
                        tuple(coupon2.getId(), "쿠폰2"),
                        tuple(coupon1.getId(), "쿠폰1")
                );
    }

    private Coupon createCoupon(String name, Long partnersId) {
        return new Coupon(name,
                new UsingDuration(
                        LocalDateTime.of(2023, 7, 10, 0, 0, 0),
                        LocalDateTime.of(2023, 7, 31, 0, 0, 0)
                ),
                5, partnersId);
    }
}