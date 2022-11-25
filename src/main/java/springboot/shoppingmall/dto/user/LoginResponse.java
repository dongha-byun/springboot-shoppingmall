package springboot.shoppingmall.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.domain.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String loginId;

    public static LoginResponse of(User user){
        return new LoginResponse("token", user.getId(), user.getLoginId());
    }
}
