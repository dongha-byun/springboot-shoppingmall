package springboot.shoppingmall.coupon.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.coupon.application.CouponQueryDto;
import springboot.shoppingmall.coupon.application.CouponQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class CouponQueryController {
    private final CouponQueryService queryService;

    @GetMapping("/partners/coupons")
    public ResponseEntity<List<CouponQueryResponse>> findCouponAll(@LoginPartner AuthorizedPartner partner) {
        List<CouponQueryDto> couponDtoAll = queryService.findCouponAll(partner.getId());
        List<CouponQueryResponse> responses = couponDtoAll.stream()
                .map(CouponQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }
}
