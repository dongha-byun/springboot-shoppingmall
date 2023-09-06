package springboot.shoppingmall.userservice.user.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserGrade;
import springboot.shoppingmall.userservice.user.domain.UserGradeInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserGradeInfoDto {
    private Long userId;
    private String userName;
    private LocalDateTime signUpDate;
    private UserGrade currentUserGrade;
    private UserGrade nextUserGrade;
    private int orderCount;
    private int amount;

    public static UserGradeInfoDto of(User user) {
        UserGradeInfo userGradeInfo = user.getUserGradeInfo();
        return new UserGradeInfoDto(
                user.getId(), user.getUserName(),
                user.getSignUpDate(), userGradeInfo.getGrade(),
                userGradeInfo.getGrade().nextGrade().orElse(null),
                userGradeInfo.getOrderCount(), userGradeInfo.getAmount()
        );
    }
}
