package springboot.shoppingmall.product.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;

public interface ProductQueryRepository extends JpaRepository<Product, Long>, CustomProductQueryRepository {

    int countByCategoryAndSubCategory(Category category, Category subCategory);
}
