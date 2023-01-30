package springboot.shoppingmall.product.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.product.domain.Product;

public interface ProductQueryRepository extends JpaRepository<Product, Long>, CustomProductQueryRepository {
}
