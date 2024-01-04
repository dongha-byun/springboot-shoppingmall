package springboot.shoppingmall.client.userservice;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springboot.shoppingmall.client.userservice.response.ResponseUserInformation;
import springboot.shoppingmall.client.userservice.request.RequestPartnerAuth;
import springboot.shoppingmall.client.userservice.response.ResponsePartnerAuthInfo;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @PostMapping(value = "/users/has-coupon")
    List<ResponseUserInformation> getUsers(@RequestBody List<Long> userIds);

    @GetMapping(value = "/users/above-grade")
    List<Long> getUserIdsAboveTheGrade(@RequestParam("targetGrade") String targetGrade);

    @PostMapping(value = "/auth")
    ResponsePartnerAuthInfo authPartner(@RequestBody RequestPartnerAuth requestPartnerAuth);

    @GetMapping(value = "/users/{userId}/discount-rate")
    int getDiscountRate(@PathVariable("userId") Long userId);
}
