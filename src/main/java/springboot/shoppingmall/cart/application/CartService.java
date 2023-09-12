package springboot.shoppingmall.cart.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartQueryRepository;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.cart.application.dto.CartCreateDto;
import springboot.shoppingmall.cart.application.dto.CartDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;
    private final ProductFinder productFinder;

    @Transactional
    public CartDto create(Long userId, CartCreateDto cartCreateDto) {
        Product product = productFinder.findProductById(cartCreateDto.getProductId());
        Cart saveCart = cartRepository.save(
                new Cart(cartCreateDto.getQuantity(), product, userId)
        );
        return CartDto.of(saveCart);
    }

    public List<CartDto> findAllByUser(Long userId) {
        return cartQueryRepository.findAllCartByUserId(userId);
    }

    @Transactional
    public void delete(Long userId, Long cartId) {
        cartRepository.deleteByIdAndUserId(cartId, userId);
    }

}
