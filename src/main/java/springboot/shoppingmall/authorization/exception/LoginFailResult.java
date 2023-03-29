package springboot.shoppingmall.authorization.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginFailResult {
    private String message;
    private int loginFailCount;

    public LoginFailResult(String message) {
        this.message = message;
    }

    public LoginFailResult(String message, int loginFailCount) {
        this.message = message;
        this.loginFailCount = loginFailCount;
    }
}
