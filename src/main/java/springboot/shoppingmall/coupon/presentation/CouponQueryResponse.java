package springboot.shoppingmall.coupon.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.application.CouponQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CouponQueryResponse {
    private Long id;
    private String name;
    private String fromDate;
    private String toDate;
    private int discountRate;

    public static CouponQueryResponse of(CouponQueryDto dto) {
        return CouponQueryResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .fromDate(DateUtils.toStringOfLocalDateTIme(dto.getFromDate(), "yyyy-MM-dd"))
                .toDate(DateUtils.toStringOfLocalDateTIme(dto.getToDate(), "yyyy-MM-dd"))
                .discountRate(dto.getDiscountRate())
                .build();
    }
}
