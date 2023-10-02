package springboot.shoppingmall.providers.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.providers.web")
public class ProviderExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResult(e.getMessage()));
    }
}
