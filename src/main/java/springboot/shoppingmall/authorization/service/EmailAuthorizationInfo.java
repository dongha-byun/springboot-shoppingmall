package springboot.shoppingmall.authorization.service;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.authorization.domain.Email;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthorizationInfo {
    private String email;
    private LocalDateTime expireTime;
    private String message;

    public static EmailAuthorizationInfo of(Email email, EmailAuthorizationCode code, String message) {
        return new EmailAuthorizationInfo(email.getValue(), code.getExpireTime(), message);
    }
}
