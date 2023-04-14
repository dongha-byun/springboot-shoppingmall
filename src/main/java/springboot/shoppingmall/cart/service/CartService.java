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
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserFinder userFinder;
    private final ProductFinder productFinder;

    @Transactional
    public CartResponse create(Long userId, CartRequest cartRequest) {
        Product product = productFinder.findProductById(cartRequest.getProductId());
        User user = userFinder.findUserById(userId);
        Cart saveCart = cartRepository.save(
                new Cart(cartRequest.getQuantity(), product, user)
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
