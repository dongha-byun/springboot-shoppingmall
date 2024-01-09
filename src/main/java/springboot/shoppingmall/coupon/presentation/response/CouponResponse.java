package springboot.shoppingmall.coupon.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponResponse {
    private Long couponId;
    private String message;
}
