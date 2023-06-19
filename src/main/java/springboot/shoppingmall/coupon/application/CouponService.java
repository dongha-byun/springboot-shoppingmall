package springboot.shoppingmall.coupon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserFinder userFinder;

    @Transactional
    public Long create(CouponCreateDto couponCreateDto) {
        List<User> targetUserList = userFinder.findUserOverTheUserGrade(couponCreateDto.getGrade());
        Coupon savedCoupon = couponRepository.save(
                Coupon.create(
                        couponCreateDto.getFromDate(),
                        couponCreateDto.getToDate(),
                        couponCreateDto.getDiscountRate(),
                        couponCreateDto.getPartnersId()
                )
        );
        for (User user : targetUserList) {
            savedCoupon.addUserCoupon(
                    UserCoupon.create(user.getId(), savedCoupon)
            );
        }

        return savedCoupon.getId();
    }
}
