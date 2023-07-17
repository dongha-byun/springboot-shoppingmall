package springboot.shoppingmall.coupon.presentation;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.application.CouponCreateDto;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponCreateRequest {
    @NotBlank(message = "쿠폰명은 필수 항목 입니다.")
    private String name;
    private String fromDate;
    private String toDate;
    private String userGrade;
    private int discountRate;

    public CouponCreateDto toDto(Long partnerId) {
        return new CouponCreateDto(
                name, DateUtils.toStartDate(fromDate), DateUtils.toEndDate(toDate),
                UserGrade.valueOf(userGrade), discountRate, partnerId
        );
    }
}
