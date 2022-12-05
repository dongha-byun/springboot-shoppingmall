package springboot.shoppingmall.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String name;
    private String loginId;
    private String password;
    private String confirmPassword;
    private String telNo;

    public SignUpRequest(String name, String loginId, String password, String confirmPassword, String telNo) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.telNo = telNo;
    }

    public static User to(SignUpRequest signUpRequest){
        return new User(signUpRequest.getName(), signUpRequest.getLoginId(), signUpRequest.getPassword(),
                signUpRequest.getTelNo());
    }
}
