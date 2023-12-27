package springboot.shoppingmall.product.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springboot.shoppingmall.product.presentation.NoThumbnailException;
import springboot.shoppingmall.product.presentation.ProductApiController;

@RestControllerAdvice(assignableTypes = ProductApiController.class)
public class ProductExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> noThumbnailException(NoThumbnailException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(e.getMessage())
        );
    }
}
