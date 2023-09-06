package springboot.shoppingmall.userservice.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.domain.UserGrade;
import springboot.shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserGradeInfoResponse {
    private Long userId;
    private String userName;
    private String signUpDate;
    private String currentUserGrade;
    private int gradeDiscountRate;
    private String nextUserGrade;
    private int remainedOrderCountForNextGrade;
    private int remainedAmountsForNextGrade;

    public static UserGradeInfoResponse to(UserGradeInfoDto dto) {
        UserGrade nextGrade = dto.getNextUserGrade();
        if(nextGrade == null) {
            return new UserGradeInfoResponse(
                    dto.getUserId(), dto.getUserName(),
                    DateUtils.toStringOfLocalDateTIme(dto.getSignUpDate(), "yyyy-MM-dd"),
                    dto.getCurrentUserGrade().getGradeName(),
                    dto.getCurrentUserGrade().getDiscountRate(),
                    null,
                    0,
                    0
            );
        }

        int remainedOrderCountForNextGrade = nextGrade.getMinOrderCondition() - dto.getOrderCount();
        int remainedAmountsForNextGrade = nextGrade.getMinAmountCondition() - dto.getAmount();
        return new UserGradeInfoResponse(
                dto.getUserId(), dto.getUserName(),
                DateUtils.toStringOfLocalDateTIme(dto.getSignUpDate(), "yyyy-MM-dd"),
                dto.getCurrentUserGrade().getGradeName(),
                dto.getCurrentUserGrade().getDiscountRate(),
                nextGrade.getGradeName(),
                remainedOrderCountForNextGrade,
                remainedAmountsForNextGrade
        );

    }
}
