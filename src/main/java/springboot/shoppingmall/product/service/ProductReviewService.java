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
import springboot.shoppingmall.product.dto.ProductReviewDto;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ProductFinder productFinder;

    public List<ProductReviewResponse> findAllReview(Long productId) {
        List<ProductReviewDto> reviewDtos = reviewRepository.findAllProductReview(productId);
        return reviewDtos.stream()
                .map(
                        productReviewDto -> new ProductReviewResponse(productReviewDto.getId(),
                                productReviewDto.getContent(),
                                productReviewDto.getWriteDate(),
                                productReviewDto.getUserName())
                ).collect(Collectors.toList());
    }

    public List<ProductUserReviewResponse> findAllUserReview(Long userId) {
        List<ProductReview> reviews = reviewRepository.findAllByUserId(userId);

        return reviews.stream()
                .map(ProductUserReviewResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public ProductUserReviewResponse createProductReview(Long userId, Long productId, ProductReviewRequest productReviewRequest) {
        Product product = productFinder.findProductById(productId);

        if(reviewRepository.existsByUserIdAndProduct(userId, product)) {
            throw new IllegalArgumentException("이미 작성된 리뷰가 있습니다.");
        }

        ProductReview productReview = ProductReview.builder()
                .content(productReviewRequest.getContent())
                .score(productReviewRequest.getScore())
                .product(product)
                .userId(userId)
                .build();

        ProductReview savedReview = reviewRepository.save(productReview);
        return ProductUserReviewResponse.of(savedReview);
    }

    @Transactional
    public void deleteProductReview(Long userId, Long reviewId) {
        ProductReview productReview = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 리뷰 입니다.")
                );
        if(!productReview.getUserId().equals(userId)) {
            throw new IllegalArgumentException("고객님께서 등록하신 리뷰가 아니라 삭제가 불가능 합니다.");
        }
        reviewRepository.delete(productReview);
    }
}
