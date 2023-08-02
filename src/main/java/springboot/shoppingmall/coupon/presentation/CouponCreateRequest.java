package springboot.shoppingmall.coupon.presentation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "유효기간 시작일은 필수 항목 입니다.")
    private String fromDate;
    @NotBlank(message = "유효기간 종료일은 필수 항목 입니다.")
    private String toDate;
    @NotBlank(message = "지급대상은 필수 항목 입니다.")
    private String userGrade;
    @Min(value = 1, message = "1% 이상 할인율을 책정해야 합니다.")
    private int discountRate;

    public CouponCreateDto toDto(Long partnerId) {
        return new CouponCreateDto(
                name, DateUtils.toStartDate(fromDate), DateUtils.toEndDate(toDate),
                UserGrade.valueOf(userGrade), discountRate, partnerId
        );
    }
}
