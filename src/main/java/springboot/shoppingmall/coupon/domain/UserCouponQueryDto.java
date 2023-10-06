package springboot.shoppingmall.coupon.domain;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserCouponQueryDto {
    private Long userId;
    private String userName;
    private String userGrade;
    private LocalDateTime usingDate;

    @QueryProjection
    public UserCouponQueryDto(Long userId, String userName, String userGrade, LocalDateTime usingDate) {
        this.userId = userId;
        this.userName = userName;
        this.userGrade = userGrade;
        this.usingDate = usingDate;
    }
}
