package springboot.shoppingmall.cart.domain;

import java.util.List;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;

public interface CartQueryRepository {
    List<CartQueryDto> findAllCartByUserId(Long userId);
}
