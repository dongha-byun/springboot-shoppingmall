package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.utils.DateUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String telNo;
    private String email;
    private String signUpDate;

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getId(), user.getUserName(), user.telNo(),
                user.getEmail(), DateUtils.toStringOfLocalDateTIme(user.getSignUpDate(), "yyyy-MM-dd")
        );
    }
}
