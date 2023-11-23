package springboot.shoppingmall.coupon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserServiceClient userServiceClient;

    public Long create(CouponCreateDto couponCreateDto) {
        List<Long> targetUserList = userServiceClient.getUserIdsAboveTheGrade(couponCreateDto.getGrade());
        Coupon savedCoupon = couponRepository.save(couponCreateDto.toEntity());
        targetUserList.forEach(
                savedCoupon::addUserCoupon
        );

        return savedCoupon.getId();
    }
}
