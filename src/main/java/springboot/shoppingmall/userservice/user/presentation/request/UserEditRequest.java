package springboot.shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.domain.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserEditRequest {
    private String password;
    private String telNo;

    public static User to(UserEditRequest userEditRequest) {
        return new User(null, null, userEditRequest.getPassword(), userEditRequest.getTelNo());
    }
}