package springboot.shoppingmall.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
