package springboot.shoppingmall.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQnaRepository extends JpaRepository<ProductQna, Long>, CustomProductQnaRepository {
}
