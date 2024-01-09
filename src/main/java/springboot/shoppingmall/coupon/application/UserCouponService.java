package springboot.shoppingmall.coupon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public void recoveryCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자가 소유한 쿠폰이 조회되지 않습니다.")
                );
        userCoupon.recovery();
    }
}
