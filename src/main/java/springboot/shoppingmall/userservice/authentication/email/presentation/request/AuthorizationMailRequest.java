package springboot.shoppingmall.userservice.authentication.email.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthorizationMailRequest {
    private String email;

    public Email toValue() {
        return new Email(this.email);
    }
}
