package springboot.shoppingmall.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.user.domain.User;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> findAllByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);
}
