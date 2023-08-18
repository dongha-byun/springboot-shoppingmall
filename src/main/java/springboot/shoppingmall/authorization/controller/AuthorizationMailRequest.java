package springboot.shoppingmall.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.authorization.domain.Email;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthorizationMailRequest {
    private String email;

    public Email toValue() {
        return new Email(this.email);
    }
}
