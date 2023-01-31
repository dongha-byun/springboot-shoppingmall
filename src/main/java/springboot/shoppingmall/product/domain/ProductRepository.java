package springboot.shoppingmall.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.category.domain.Category;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
