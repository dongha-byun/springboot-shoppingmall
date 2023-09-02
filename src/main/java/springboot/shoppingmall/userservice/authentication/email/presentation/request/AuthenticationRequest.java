package springboot.shoppingmall.userservice.authentication.email.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationRequest {
    private String email;
    private String code;

    public Email getEmailValue() {
        return new Email(this.email);
    }

    public EmailAuthenticationCode getCodeValue() {
        return new EmailAuthenticationCode(this.code);
    }
}
