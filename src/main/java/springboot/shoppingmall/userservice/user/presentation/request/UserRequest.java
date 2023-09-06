package springboot.shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String loginId;
    private String name;
    private String telNo;
    private String password;
    private String confirmPassword;

    public static User to(UserRequest request) {
        return new User(request.getName(), request.getLoginId(), request.getPassword(), request.getTelNo());
    }

}