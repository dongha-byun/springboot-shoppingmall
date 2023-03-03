package springboot.shoppingmall.product.query.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

public interface ProductQueryRepository extends JpaRepository<Product, Long>, CustomProductQueryRepository {
}
