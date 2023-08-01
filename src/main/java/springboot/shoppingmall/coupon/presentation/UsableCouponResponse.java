package springboot.shoppingmall.coupon.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.application.UsableCouponDto;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsableCouponResponse {
    private Long id;
    private String name;
    private String fromDate;
    private String toDate;
    private int discountRate;

    public static UsableCouponResponse of(UsableCouponDto dto) {
        return new UsableCouponResponse(
                dto.getId(), dto.getName(),
                DateUtils.toStringOfLocalDateTIme(dto.getFromDate(), "yyyy-MM-dd"),
                DateUtils.toStringOfLocalDateTIme(dto.getToDate(), "yyyy-MM-dd"),
                dto.getDiscountRate()
        );
    }
}
