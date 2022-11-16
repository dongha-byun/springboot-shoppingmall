package springboot.shoppingmall.dto.user;

import lombok.Data;

@Data
public class LoginRequest {

    private String loginId;
    private String password;

    public LoginRequest() {
    }
}
