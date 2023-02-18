package springboot.shoppingmall.cart.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.cart.web.CartRequest;
import springboot.shoppingmall.cart.web.CartResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse create(Long userId, CartRequest cartRequest) {
        User user = findUserById(userId);

        Cart saveCart = cartRepository.save(
                new Cart(cartRequest.getQuantity(), cartRequest.getProductId(), user)
        );
        return CartResponse.of(saveCart);
    }

    public List<CartResponse> findAllByUser(Long userId) {
        User user = findUserById(userId);
        return user.getBaskets().stream()
                .map(CartResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Long id) {
        User user = findUserById(userId);
        Cart cart = findBasketById(id);

        user.removeBasket(cart);
    }

    private Cart findBasketById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("장바구니 조회 실패")
                );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );
    }
}
