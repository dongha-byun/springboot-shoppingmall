package springboot.shoppingmall.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.domain.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
