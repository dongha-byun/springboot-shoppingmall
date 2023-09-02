package springboot.shoppingmall.userservice.authentication.email.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springboot.shoppingmall.userservice.authentication.email.presentation.response.AuthenticationFailResponse;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.userservice.authentication.email.presentation")
public class EmailAuthenticationControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthenticationFailResponse> illegalArgumentException(IllegalArgumentException e) {
        AuthenticationFailResponse response = AuthenticationFailResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
