package springboot.shoppingmall.coupon.application;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.UserGrade;

@NoArgsConstructor
@Getter
public class CouponQueryDto {
    private Long id;
    private String name;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int discountRate;

    @QueryProjection
    public CouponQueryDto(Long id, String name, LocalDateTime fromDate, LocalDateTime toDate, int discountRate) {
        this.id = id;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.discountRate = discountRate;
    }
}
