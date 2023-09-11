package springboot.shoppingmall.coupon.client;

import java.util.List;

public interface UserCouponService {
    List<Long> getUserIdsAboveTheGrade(String targetGrade);
}
