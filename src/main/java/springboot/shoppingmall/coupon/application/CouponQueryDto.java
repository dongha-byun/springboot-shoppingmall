package springboot.shoppingmall.coupon.application;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.domain.Coupon;

@NoArgsConstructor
@Getter
public class CouponQueryDto {
    private Long id;
    private String name;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int discountRate;

    @Builder
    @QueryProjection
    public CouponQueryDto(Long id, String name, LocalDateTime fromDate, LocalDateTime toDate, int discountRate) {
        this.id = id;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.discountRate = discountRate;
    }

    public static CouponQueryDto of(Coupon coupon) {
        return CouponQueryDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .fromDate(coupon.getUsingDuration().getFromDate())
                .toDate(coupon.getUsingDuration().getToDate())
                .discountRate(coupon.getDiscountRate())
                .build();
    }
}
