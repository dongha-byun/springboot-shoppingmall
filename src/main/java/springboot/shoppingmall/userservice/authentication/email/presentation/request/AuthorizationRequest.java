package springboot.shoppingmall.userservice.authentication.email.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthorizationRequest {
    private String email;
    private String code;

    public Email getEmailValue() {
        return new Email(this.email);
    }

    public EmailAuthorizationCode getCodeValue() {
        return new EmailAuthorizationCode(this.code);
    }
}
