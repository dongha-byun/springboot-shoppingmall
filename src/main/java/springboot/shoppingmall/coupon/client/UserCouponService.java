package springboot.shoppingmall.coupon.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springboot.shoppingmall.coupon.application.dto.ResponseUserInformation;

@FeignClient(name = "user-service")
public interface UserCouponService {
    @PostMapping(value = "/users/has-coupon")
    List<ResponseUserInformation> getUsers(@RequestBody List<Long> userIds);

    @GetMapping(value = "/users/above-grade")
    List<Long> getUserIdsAboveTheGrade(@RequestParam("targetGrade") String targetGrade);
}
