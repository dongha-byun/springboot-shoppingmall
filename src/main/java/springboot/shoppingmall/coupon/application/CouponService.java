package springboot.shoppingmall.coupon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserFinder userFinder;

    public Long create(CouponCreateDto couponCreateDto) {
        List<User> targetUserList = userFinder.findUserOverTheUserGrade(couponCreateDto.getGrade());
        Coupon savedCoupon = couponRepository.save(couponCreateDto.toEntity());
        targetUserList.forEach(
                user -> savedCoupon.addUserCoupon(user.getId())
        );

        return savedCoupon.getId();
    }
}
