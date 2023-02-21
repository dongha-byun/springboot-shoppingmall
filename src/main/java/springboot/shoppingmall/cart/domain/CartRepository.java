package springboot.shoppingmall.cart.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    void deleteByIdAndUserId(Long cartId, Long userId);

    List<Cart> findAllByUserId(Long userId);
}
