package springboot.shoppingmall.coupon.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCouponQueryResponse {
    private Long userId;
    private String userName;
    private String userGrade;
    private String usingDate;

    public static UserCouponQueryResponse of(UserCouponQueryDto dto) {
        return UserCouponQueryResponse.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userGrade(dto.getUserGrade())
                .usingDate(DateUtils.toStringOfLocalDateTIme(dto.getUsingDate()))
                .build();
    }
}
