package springboot.shoppingmall.coupon.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.coupon.application.CouponQueryDto;
import springboot.shoppingmall.coupon.application.UserCouponQueryService;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

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
    public ResponseEntity<List<CouponQueryResponse>> findUsableCouponList(@AuthenticationStrategy AuthorizedUser user,
                                                                           @RequestParam("partnersId") Long partnersId) {
        List<CouponQueryDto> coupons = queryService.findUsableCouponList(user.getId(), partnersId);
        List<CouponQueryResponse> responses = coupons.stream()
                .map(CouponQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }
}
