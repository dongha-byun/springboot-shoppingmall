package springboot.shoppingmall.userservice.authentication.email.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthenticationInfo {
    private String email;
    private LocalDateTime expireTime;
    private String message;

    public static EmailAuthenticationInfo of(Email email, EmailAuthenticationCode code, String message) {
        return new EmailAuthenticationInfo(email.getValue(), code.getExpireTime(), message);
    }
}
