package springboot.shoppingmall.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Basket;
import springboot.shoppingmall.user.domain.BasketRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.BasketRequest;
import springboot.shoppingmall.user.dto.BasketResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public BasketResponse create(Long userId, BasketRequest basketRequest) {
        User user = findUserById(userId);
        Product product = findProductById(basketRequest);

        Basket saveBasket = basketRepository.save(Basket.builder()
                .quantity(basketRequest.getQuantity())
                .product(product)
                .user(user)
                .build());
        return BasketResponse.of(saveBasket);
    }

    public List<BasketResponse> findAllByUser(Long userId) {
        User user = findUserById(userId);
        return user.getBaskets().stream()
                .map(BasketResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Long id) {
        User user = findUserById(userId);
        Basket basket = findBasketById(id);

        user.removeBasket(basket);
    }

    private Basket findBasketById(Long id) {
        return basketRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("장바구니 조회 실패")
                );
    }

    private Product findProductById(BasketRequest basketRequest) {
        return productRepository.findById(basketRequest.getProductId())
                .orElseThrow(
                        () -> new IllegalArgumentException("상품 조회 실패")
                );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );
    }
}
