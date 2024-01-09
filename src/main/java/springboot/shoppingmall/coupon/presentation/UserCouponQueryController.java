package springboot.shoppingmall.coupon.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.coupon.application.UsableCouponDto;
import springboot.shoppingmall.coupon.application.UserCouponQueryService;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.presentation.response.UsableCouponResponse;
import springboot.shoppingmall.coupon.presentation.response.UserCouponQueryResponse;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class UserCouponQueryController {
    private final UserCouponQueryService queryService;

    @GetMapping("/partners/coupons/{id}/users")
    public ResponseEntity<List<UserCouponQueryResponse>> findUsersReceivedCoupon(@LoginPartner AuthorizedPartner partner,
                                                                                 @PathVariable("id") Long id) {
        List<UserCouponQueryDto> usersReceivedCoupon = queryService.findUsersReceivedCoupon(id);
        List<UserCouponQueryResponse> responses = usersReceivedCoupon.stream()
                .map(UserCouponQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/order/coupons")
    public ResponseEntity<List<UsableCouponResponse>> findUsableCouponList(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                                           @RequestParam("partnersId") Long partnersId) {
        List<UsableCouponDto> usableCouponList = queryService.findUsableCouponList(gatewayAuthInfo.getUserId(), partnersId);
        List<UsableCouponResponse> responses = usableCouponList.stream()
                .map(UsableCouponResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }
}
