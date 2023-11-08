package springboot.shoppingmall.coupon.presentation;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.coupon.application.CouponCreateDto;
import springboot.shoppingmall.coupon.application.CouponService;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@BeanValidation
@RestController
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CouponResponse> create(@LoginPartner AuthorizedPartner partner,
                                                 @Valid @RequestBody CouponCreateRequest couponCreateRequest,
                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BeanValidationException(bindingResult);
        }

        CouponCreateDto couponCreateDto = couponCreateRequest.toDto(partner.getId());
        Long couponId = couponService.create(couponCreateDto);

        return ResponseEntity.created(URI.create("/coupons/"+couponId)).body(
                new CouponResponse(couponId, "쿠폰이 정상적으로 등록되었습니다.")
        );
    }
}
