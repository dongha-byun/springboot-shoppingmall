package springboot.shoppingmall.cart.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.cart.web.CartRequest;
import springboot.shoppingmall.cart.web.CartResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {
    private final CartRepository cartRepository;

    @Transactional
    public CartResponse create(Long userId, CartRequest cartRequest) {
        Cart saveCart = cartRepository.save(
                new Cart(cartRequest.getQuantity(), cartRequest.getProductId(), userId)
        );
        return CartResponse.of(saveCart);
    }

    public List<CartResponse> findAllByUser(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        return carts.stream()
                .map(CartResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Long cartId) {
        cartRepository.deleteByIdAndUserId(cartId, userId);
    }

}
