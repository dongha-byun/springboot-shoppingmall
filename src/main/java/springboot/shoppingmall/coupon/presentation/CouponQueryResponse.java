package springboot.shoppingmall.coupon.presentation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.application.CouponQueryDto;

@NoArgsConstructor
@Getter
public class CouponQueryResponse {
    private Long id;
    private String name;
    private String fromDate;
    private String toDate;

    @Builder
    public CouponQueryResponse(Long id, String name, String fromDate, String toDate) {
        this.id = id;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static CouponQueryResponse of(CouponQueryDto dto) {
        return CouponQueryResponse.builder()
                .build();
    }
}
