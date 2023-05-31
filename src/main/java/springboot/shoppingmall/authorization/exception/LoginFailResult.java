package springboot.shoppingmall.authorization.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginFailResult {
    private int code;
    private String message;

    public LoginFailResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public LoginFailResult(String message) {
        this.message = message;
    }
}
