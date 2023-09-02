package springboot.shoppingmall.userservice.authentication.email.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationFailResponse {
    private String message;
}
