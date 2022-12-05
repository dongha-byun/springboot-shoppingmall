package springboot.shoppingmall.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
