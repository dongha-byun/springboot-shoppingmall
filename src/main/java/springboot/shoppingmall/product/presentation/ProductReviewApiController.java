package springboot.shoppingmall.product.presentation;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
import springboot.shoppingmall.product.application.dto.ProductReviewDto;
import springboot.shoppingmall.product.application.dto.ProductUserReviewDto;
import springboot.shoppingmall.product.presentation.request.ProductReviewRequest;
import springboot.shoppingmall.product.presentation.response.ProductReviewResponse;
import springboot.shoppingmall.product.presentation.response.ProductUserReviewResponse;
import springboot.shoppingmall.product.exception.ContentNotBlankException;
import springboot.shoppingmall.product.application.ProductReviewService;
import springboot.shoppingmall.product.application.ThumbnailInfo;
import springboot.shoppingmall.product.application.dto.ProductReviewCreateDto;

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/orders/{orderId}/{orderItemId}/products/{productId}/reviews")
    public ResponseEntity<ProductUserReviewResponse> createReview(@AuthenticationStrategy AuthorizedUser user,
                                                                  @PathVariable("orderId") Long orderId,
                                                                  @PathVariable("orderItemId") Long orderItemId,
                                                                  @PathVariable("productId") Long productId,
                                                                  @RequestPart(name = "data") @Valid ProductReviewRequest reviewRequest,
                                                                  BindingResult bindingResult,
                                                                  @RequestPart(name = "file", required = false) List<MultipartFile> images) throws IOException {
        if(bindingResult.hasErrors()){
            throw new ContentNotBlankException("리뷰 내용은 필수 입니다.");
        }

        List<ThumbnailInfo> reviewImages = thumbnailFileService.save(images);
        ProductReviewCreateDto createDto = reviewRequest.toDto();
        ProductUserReviewDto dto = productReviewService.createProductReview(
                user.getId(), orderItemId, productId, createDto, reviewImages
        );
        ProductUserReviewResponse reviewResponse = ProductUserReviewResponse.of(dto);
        return ResponseEntity.created(URI.create("/products/" + productId + "/reviews/" + reviewResponse.getId())).body(reviewResponse);
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ProductReviewResponse>> findAllByProduct(@PathVariable("productId") Long productId) {
        List<ProductReviewDto> dtos = productReviewService.findAllReview(productId);
        List<ProductReviewResponse> reviews = dtos.stream()
                .map(ProductReviewResponse::of)
                .collect(Collectors.toList());
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
        List<ProductUserReviewDto> dtos = productReviewService.findAllUserReview(user.getId());
        List<ProductUserReviewResponse> reviews = dtos.stream()
                .map(ProductUserReviewResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(reviews);
    }

}
