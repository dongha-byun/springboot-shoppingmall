package springboot.shoppingmall.common.validation.bean;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = BeanValidation.class)
public class BeanValidationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BeanValidationErrorResponse> beanValidationException(BeanValidationException e) {
        return ResponseEntity.badRequest().body(
                new BeanValidationErrorResponse(e.getErrors())
        );
    }
}
