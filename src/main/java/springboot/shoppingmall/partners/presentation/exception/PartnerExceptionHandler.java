package springboot.shoppingmall.partners.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.partners.presentation")
public class PartnerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResult(e.getMessage()));
    }
}
