package springboot.shoppingmall.cart.domain;

import java.util.List;
import springboot.shoppingmall.cart.dto.CartDto;

public interface CartQueryRepository {
    List<CartDto> findAllCartByUserId(Long userId);
}
