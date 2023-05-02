package springboot.shoppingmall.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springboot.shoppingmall.authorization.exception.ErrorCode;
import springboot.shoppingmall.pay.web.ErrorResponse;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.order.controller")
public class OrderExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> overQuantityException(OverQuantityException e) {
        int code = ErrorCode.OVER_QUANTITY.getCode();
        String message = ErrorCode.OVER_QUANTITY.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message, code));
    }
}
