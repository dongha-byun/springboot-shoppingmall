package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.domain.ProductReviewRepository;
import springboot.shoppingmall.product.dto.ProductReviewResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    // 1. 상품 별 목록 조회
    public List<ProductReviewResponse> findAllReview(Long id) {
        Product product = productRepository.findById(id).orElseThrow();

        return product.getReviews().stream()
                .map(ProductReviewResponse::of).collect(Collectors.toList());
    }



    // 2. 사용자가 작성한 상품 리뷰 조회

}
