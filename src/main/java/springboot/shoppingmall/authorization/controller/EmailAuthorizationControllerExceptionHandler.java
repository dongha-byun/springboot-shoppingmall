package springboot.shoppingmall.authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.authorization.controller")
public class EmailAuthorizationControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthorizationFailResponse> illegalArgumentException(IllegalArgumentException e) {
        AuthorizationFailResponse response = AuthorizationFailResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
