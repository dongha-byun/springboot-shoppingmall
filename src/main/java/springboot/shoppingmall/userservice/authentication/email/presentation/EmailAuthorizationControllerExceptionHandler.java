package springboot.shoppingmall.userservice.authentication.email.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springboot.shoppingmall.userservice.authentication.email.presentation.response.AuthorizationFailResponse;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.userservice.authentication.email.presentation")
public class EmailAuthorizationControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthorizationFailResponse> illegalArgumentException(IllegalArgumentException e) {
        AuthorizationFailResponse response = AuthorizationFailResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
