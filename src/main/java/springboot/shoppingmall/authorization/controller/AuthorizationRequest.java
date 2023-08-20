package springboot.shoppingmall.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.authorization.domain.Email;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;

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
