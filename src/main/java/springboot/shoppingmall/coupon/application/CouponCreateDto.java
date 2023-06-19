package springboot.shoppingmall.coupon.application;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.UserGrade;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CouponCreateDto {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private UserGrade grade;
    private int discountRate;
    private Long partnersId;
}
