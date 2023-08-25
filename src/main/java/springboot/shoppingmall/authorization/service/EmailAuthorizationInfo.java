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

    public static EmailAuthorizationInfo of(Email email, EmailAuthorizationCode code) {
        return new EmailAuthorizationInfo(email.getValue(), code.getExpireTime());
    }
}
