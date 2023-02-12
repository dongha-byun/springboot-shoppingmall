package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.domain.ProductReviewRepository;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ProductFinder productFinder;
    private final UserFinder userFinder;

    public List<ProductReviewResponse> findAllReview(Long productId) {
        Product product = productFinder.findProductById(productId);

        return product.getReviews().stream()
                .map(ProductReviewResponse::of).collect(Collectors.toList());
    }

    public List<ProductUserReviewResponse> findAllUserReview(Long userId) {
        User user = userFinder.findUserById(userId);
        List<ProductReview> reviews = reviewRepository.findAllByUser(user);

        return reviews.stream()
                .map(ProductUserReviewResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public ProductUserReviewResponse createProductReview(Long userId, Long productId, ProductReviewRequest productReviewRequest) {
        User user = userFinder.findUserById(userId);
        Product product = productFinder.findProductById(productId);

        ProductReview productReview = ProductReview.builder()
                .content(productReviewRequest.getContent())
                .score(productReviewRequest.getScore())
                .product(product)
                .user(user)
                .build();

        ProductReview savedReview = reviewRepository.save(productReview);
        return ProductUserReviewResponse.of(savedReview);
    }

    @Transactional
    public void deleteProductReview(Long userId, Long reviewId) {
        User user = userFinder.findUserById(userId);
        ProductReview productReview = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 리뷰 입니다.")
                );
        if(!productReview.getUser().equals(user)) {
            throw new IllegalArgumentException("고객님께서 등록하신 리뷰가 아니라 삭제가 불가능 합니다.");
        }
        reviewRepository.delete(productReview);
    }
}
