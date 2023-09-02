package springboot.shoppingmall.userservice.authentication.email.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationCode;

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
