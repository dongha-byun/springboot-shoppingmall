package springboot.shoppingmall.coupon.client;

import java.util.List;
import springboot.shoppingmall.coupon.application.dto.ResponseUserInformation;

public interface UserCouponService {
    List<Long> getUserIdsAboveTheGrade(String targetGrade);

    List<ResponseUserInformation> getUsers(List<Long> userIds);
}
