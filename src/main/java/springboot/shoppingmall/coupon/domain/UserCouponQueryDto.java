package springboot.shoppingmall.coupon.domain;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.UserGrade;

@NoArgsConstructor
@Getter
public class UserCouponQueryDto {
    private String userName;
    private UserGrade userGrade;
    private LocalDateTime usingDate;

    @QueryProjection
    public UserCouponQueryDto(String userName, UserGrade userGrade, LocalDateTime usingDate) {
        this.userName = userName;
        this.userGrade = userGrade;
        this.usingDate = usingDate;
    }
}
