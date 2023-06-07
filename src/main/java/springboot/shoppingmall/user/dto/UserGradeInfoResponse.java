package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.UserGrade;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserGradeInfoResponse {
    private Long userId;
    private String userName;
    private String enterDate;
    private String currentUserGrade;
    private String nextUserGrade;
    private int remainedOrderCountForNextGrade;
    private int remainedAmountsForNextGrade;

    public static UserGradeInfoResponse to(UserGradeInfoDto dto) {
        UserGrade nextGrade = dto.getNextUserGrade();
        if(nextGrade == null) {
            return new UserGradeInfoResponse(
                    dto.getUserId(), dto.getUserName(), null,
                    dto.getCurrentUserGrade().getGradeName(),
                    null, 0, 0
            );
        }

        int remainedOrderCountForNextGrade = nextGrade.getMinOrderCondition() - dto.getOrderCount();
        int remainedAmountsForNextGrade = nextGrade.getMinAmountCondition() - dto.getAmount();
        return new UserGradeInfoResponse(
                dto.getUserId(), dto.getUserName(), null,
                dto.getCurrentUserGrade().getGradeName(),
                nextGrade.getGradeName(),
                remainedOrderCountForNextGrade,
                remainedAmountsForNextGrade
        );

    }
}
