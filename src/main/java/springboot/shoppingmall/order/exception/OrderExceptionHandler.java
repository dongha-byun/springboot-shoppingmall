package springboot.shoppingmall.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "springboot.shoppingmall.order.controller")
public class OrderExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<OrderErrorResponse> overQuantityException(OverQuantityException e) {
        int code = OrderErrorCode.OVER_QUANTITY.getCode();
        String message = OrderErrorCode.OVER_QUANTITY.getMessage();
        return ResponseEntity.badRequest().body(new OrderErrorResponse(code, message));
    }
}
