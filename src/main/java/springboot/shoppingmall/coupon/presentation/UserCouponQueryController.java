package springboot.shoppingmall.coupon.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.coupon.application.UserCouponQueryService;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class UserCouponQueryController {
    private final UserCouponQueryService queryService;

    @GetMapping("/coupons/{id}/users")
    public ResponseEntity<List<UserCouponQueryResponse>> findUsersReceivedCoupon(@LoginPartner AuthorizedPartner partner,
                                                                                 @PathVariable("id") Long id) {
        List<UserCouponQueryDto> usersReceivedCoupon = queryService.findUsersReceivedCoupon(id);
        List<UserCouponQueryResponse> responses = usersReceivedCoupon.stream()
                .map(UserCouponQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }
}
