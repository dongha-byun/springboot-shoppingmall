package springboot.shoppingmall.product.presentation.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springboot.shoppingmall.product.exception.ContentNotBlankException;
import springboot.shoppingmall.product.presentation.ProductReviewApiController;

@RestControllerAdvice(assignableTypes = ProductReviewApiController.class)
public class ProductReviewExceptionHandler {

    @ExceptionHandler(ContentNotBlankException.class)
    public ResponseEntity<Map<String, String>> handleNotBlankException(ContentNotBlankException e) {
        Map<String, String> params = new HashMap<>();
        params.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(params);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgumentException(IllegalArgumentException e) {
        Map<String, String> params = new HashMap<>();
        params.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(params);
    }
}
