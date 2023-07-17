package springboot.shoppingmall.coupon.presentation;

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
