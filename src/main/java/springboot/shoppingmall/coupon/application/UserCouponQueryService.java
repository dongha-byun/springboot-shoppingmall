package springboot.shoppingmall.coupon.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.domain.UserCouponQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserCouponQueryService {
    private final UserCouponQueryRepository queryRepository;
    private final CouponRepository couponRepository;

    public List<UserCouponQueryDto> findUsersReceivedCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(
                        () -> new IllegalArgumentException("쿠폰 정보 조회 실패")
                );
        return queryRepository.findAllUserReceivedCoupon(coupon);
    }

    public List<CouponQueryDto> findUsableCouponList(Long userId, Long partnersId) {
        List<Coupon> coupons = queryRepository.findUsableCouponList(userId, partnersId);
        return coupons.stream()
                .map(CouponQueryDto::of)
                .collect(Collectors.toList());
    }
}
