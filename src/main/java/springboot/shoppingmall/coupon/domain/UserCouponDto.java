package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserCouponDto {
    private Long userId;
    private LocalDateTime usingDate;
}
