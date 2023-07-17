package springboot.shoppingmall.coupon.presentation;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.coupon.application.CouponCreateDto;
import springboot.shoppingmall.coupon.application.CouponService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CouponResponse> create(@LoginPartner AuthorizedPartner partner,
                                                 @Valid @RequestBody CouponCreateRequest couponCreateRequest) {
        CouponCreateDto couponCreateDto = couponCreateRequest.toDto(partner.getId());
        Long couponId = couponService.create(couponCreateDto);

        return ResponseEntity.created(URI.create("/coupons/"+couponId)).body(
                new CouponResponse(couponId, "쿠폰이 정상적으로 등록되었습니다.")
        );
    }
}
