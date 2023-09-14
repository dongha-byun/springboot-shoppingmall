package springboot.shoppingmall.cart.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.cart.application.dto.CartCreateDto;
import springboot.shoppingmall.cart.application.dto.CartDto;

@RequiredArgsConstructor
@Transactional
@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartDto create(Long userId, CartCreateDto cartCreateDto) {
        Cart saveCart = cartRepository.save(
                new Cart(cartCreateDto.getQuantity(), cartCreateDto.getProductId(), userId)
        );
        return CartDto.of(saveCart);
    }

    public void delete(Long userId, Long cartId) {
        cartRepository.deleteByIdAndUserId(cartId, userId);
    }

}
