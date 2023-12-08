package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;

@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @MockBean
    UserServiceClient userServiceClient;

    @Test
    @DisplayName("쿠폰 생성 - 특정 회원등급 이상의 회원들에게 쿠폰을 발급한다.")
    void create_coupon_for_user() {
        // 단골회원(REGULAR) 등급 이상인 회원들에게
        // 사용기한이 2023-05-28 ~ 2023-07-28 인
        // 할인율 7%의 쿠폰을 발급해준다.
        // given
        String name = "기념 할인 쿠폰";
        LocalDateTime fromDate = LocalDateTime.of(2023, 5, 28, 0, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2023, 7, 28, 23, 59, 59);
        int discountRate = 5;
        Long partnersId = 1L;
        CouponCreateDto couponCreateDto = new CouponCreateDto(
                name, fromDate, toDate, "REGULAR", discountRate, partnersId
        );

        when(userServiceClient.getUserIdsAboveTheGrade(any())).thenReturn(
                Arrays.asList(100L, 200L, 300L)
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