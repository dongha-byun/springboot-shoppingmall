package springboot.shoppingmall.authorization.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.authorization.controller")
public class LoginExceptionHandler {

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<LoginFailResult> wrongPasswordException(WrongPasswordException e) {
        LoginFailResult loginFailResult = new LoginFailResult(e.getMessage());
        return ResponseEntity.badRequest().body(loginFailResult);
    }

    @ExceptionHandler(TryLoginLockedUserException.class)
    public ResponseEntity<LoginFailResult> tryLoginLockedUserException(TryLoginLockedUserException e) {
        LoginFailResult loginFailResult = new LoginFailResult(e.getMessage());
        return ResponseEntity.badRequest().body(loginFailResult);
    }
}
