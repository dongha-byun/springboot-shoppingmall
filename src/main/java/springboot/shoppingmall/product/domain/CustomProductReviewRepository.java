package springboot.shoppingmall.product.domain;

import java.util.List;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;

public interface CustomProductReviewRepository {

    List<ProductReviewDto> findAllProductReview(Long productId);
}
