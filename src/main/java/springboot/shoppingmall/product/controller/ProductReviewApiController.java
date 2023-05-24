package springboot.shoppingmall.product.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.validator.OrderValidator;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.product.exception.ContentNotBlankException;
import springboot.shoppingmall.product.service.ProductReviewService;

@RequiredArgsConstructor
@RestController
public class ProductReviewApiController {

    private final ProductReviewService productReviewService;
    private final OrderValidator orderValidator;

    @ExceptionHandler(ContentNotBlankException.class)
    public ResponseEntity<ProductUserReviewResponse> handleNotBlankException(ContentNotBlankException exception) {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/orders/{orderId}/{orderItemId}/products/{productId}/reviews")
    public ResponseEntity<ProductUserReviewResponse> createReview(@AuthenticationStrategy AuthorizedUser user,
                                                                  @PathVariable("orderId") Long orderId,
                                                                  @PathVariable("orderItemId") Long orderItemId,
                                                                  @PathVariable("productId") Long productId,
                                                                  @Valid @RequestBody ProductReviewRequest reviewRequest,
                                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ContentNotBlankException();
        }

        ProductUserReviewResponse reviewResponse =
                productReviewService.createProductReview(user.getId(), user.getLoginId(), orderItemId, productId, reviewRequest);
        return ResponseEntity.created(URI.create("/products/" + productId + "/reviews/" + reviewResponse.getId())).body(reviewResponse);
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ProductReviewResponse>> findAllByProduct(@PathVariable("productId") Long productId) {
        List<ProductReviewResponse> reviews = productReviewService.findAllReview(productId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/users/reviews/{reviewId}")
    public ResponseEntity<ProductReviewResponse> deleteReview(@AuthenticationStrategy AuthorizedUser user,
                                                              @PathVariable("reviewId") Long reviewId) {
        productReviewService.deleteProductReview(user.getId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/reviews")
    public ResponseEntity<List<ProductUserReviewResponse>> findAllByUser(@AuthenticationStrategy AuthorizedUser user) {
        List<ProductUserReviewResponse> reviews = productReviewService.findAllUserReview(user.getId());
        return ResponseEntity.ok(reviews);
    }

}
