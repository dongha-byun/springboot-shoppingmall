package springboot.shoppingmall.authorization.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginFailResult {
    private String message;

    public LoginFailResult(String message) {
        this.message = message;
    }
}
