package springboot.shoppingmall.product.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.validator.OrderValidator;
import springboot.shoppingmall.product.application.dto.ProductUserReviewDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.domain.ProductReviewImage;
import springboot.shoppingmall.product.domain.ProductReviewRepository;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;
import springboot.shoppingmall.product.presentation.response.ProductReviewResponse;
import springboot.shoppingmall.product.presentation.response.ProductUserReviewResponse;
import springboot.shoppingmall.product.application.dto.ProductReviewCreateDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ProductFinder productFinder;
    private final OrderFinder orderFinder;
    private final OrderValidator orderValidator;

    public List<ProductReviewDto> findAllReview(Long productId) {
        return reviewRepository.findAllProductReview(productId);
    }

    public List<ProductUserReviewDto> findAllUserReview(Long userId) {
        List<ProductReview> reviews = reviewRepository.findAllByUserId(userId);

        return reviews.stream()
                .map(ProductUserReviewDto::of).collect(Collectors.toList());
    }

    @Transactional
    public ProductUserReviewDto createProductReview(Long userId, Long orderItemId, Long productId,
                                                    ProductReviewCreateDto createDto, List<ThumbnailInfo> images) {
        orderValidator.validateOrderIsEnd(orderItemId);

        Product product = productFinder.findProductById(productId);
        if(reviewRepository.existsByUserIdAndProduct(userId, product)) {
            throw new IllegalArgumentException("이미 작성된 리뷰가 있습니다.");
        }
        List<ProductReviewImage> reviewImages = images.stream()
                .map(thumbnailInfo -> new ProductReviewImage(thumbnailInfo.getStoredFileName(),
                        thumbnailInfo.getViewFileName()))
                .collect(Collectors.toList());

        ProductReview productReview = ProductReview.builder()
                .content(createDto.getContent())
                .score(createDto.getScore())
                .product(product)
                .userId(userId)
                .images(reviewImages)
                .build();
        ProductReview savedReview = reviewRepository.save(productReview);

        // 리뷰 작성 시, 해당 주문은 구매확정 처리가 된다.
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.finish();

        // 평점 총합 / 리뷰 갯수 -> 평점 갱신
        // 비동기 처리하자... 여기서 할 일이 아닌가베
        //product.refreshScore();

        return ProductUserReviewDto.of(savedReview);
    }

    @Transactional
    public void deleteProductReview(Long userId, Long reviewId) {
        ProductReview productReview = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 리뷰 입니다.")
                );
        if(!productReview.isWriter(userId)) {
            throw new IllegalArgumentException("고객님께서 등록하신 리뷰가 아니라 삭제가 불가능 합니다.");
        }
        reviewRepository.delete(productReview);
    }
}
