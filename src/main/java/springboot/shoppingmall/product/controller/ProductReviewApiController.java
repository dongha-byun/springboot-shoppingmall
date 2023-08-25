package springboot.shoppingmall.product.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.product.exception.ContentNotBlankException;
import springboot.shoppingmall.product.service.ProductReviewService;
import springboot.shoppingmall.product.service.ThumbnailInfo;
import springboot.shoppingmall.product.service.dto.ProductReviewCreateDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductReviewApiController {

    private final ProductReviewService productReviewService;
    private final ThumbnailFileService thumbnailFileService;

    @ExceptionHandler(ContentNotBlankException.class)
    public ResponseEntity<String> handleNotBlankException(ContentNotBlankException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @PostMapping("/orders/{orderId}/{orderItemId}/products/{productId}/reviews")
    public ResponseEntity<ProductUserReviewResponse> createReview(@AuthenticationStrategy AuthorizedUser user,
                                                                  @PathVariable("orderId") Long orderId,
                                                                  @PathVariable("orderItemId") Long orderItemId,
                                                                  @PathVariable("productId") Long productId,
                                                                  @Valid @RequestPart(name = "data") ProductReviewRequest reviewRequest,
                                                                  @RequestPart(name = "file", required = false) List<MultipartFile> images,
                                                                  BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()){
            throw new ContentNotBlankException("리뷰 내용은 필수 입니다.");
        }

        List<ThumbnailInfo> reviewImages = thumbnailFileService.save(images);
        ProductReviewCreateDto createDto = reviewRequest.toDto();
        ProductUserReviewResponse reviewResponse =
                productReviewService.createProductReview(user.getId(), orderItemId, productId, createDto, reviewImages);
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
