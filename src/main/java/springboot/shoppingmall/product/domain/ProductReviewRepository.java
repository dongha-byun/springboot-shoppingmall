package springboot.shoppingmall.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, CustomProductReviewRepository {

    List<ProductReview> findAllByUserId(Long userId);

    boolean existsByUserIdAndProduct(Long userId, Product product);
}
