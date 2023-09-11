package springboot.shoppingmall.cart.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.cart.dto.CartDto;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.cart.web.CartRequest;
import springboot.shoppingmall.cart.web.CartResponse;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;

@Transactional
@SpringBootTest
class CartServiceTest {

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    Product product2;
    Product product3;
    Long userId = 10L;

    @BeforeEach
    void setUp(){
        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));

        Provider provider = providerRepository.save(
                new Provider("판매업체", "판매대표", "판매업체 주소", "031-222-3311", "222-33-112233", "test1", "test1!")
        );

        product = productRepository.save(
                new Product("상품 1", 22000, 10, category, subCategory,
                        provider.getId(), "product_stored_file_name1", "product_view_file_name1",
                        "상품 1 설명 입니다.", "test-product-code1")
        );
        product2 = productRepository.save(
                new Product("상품 2", 32000, 5, category, subCategory,
                        provider.getId(), "product_stored_file_name2", "product_view_file_name2",
                        "상품 2 설명 입니다.", "test-product-code2")
        );
        product3 = productRepository.save(
                new Product("상품 3", 42000, 3, category, subCategory,
                        provider.getId(), "product_stored_file_name3", "product_view_file_name3",
                        "상품 3 설명 입니다.", "test-product-code3")
        );
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void createCartTest(){
        // given
        CartRequest cartRequest = new CartRequest(2, product.getId());

        // when
        CartResponse cartResponse = cartService.create(userId, cartRequest);

        // then
        assertThat(cartResponse.getId()).isNotNull();
        assertThat(cartResponse.getQuantity()).isEqualTo(2);
        assertThat(cartResponse.getProductId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("장바구니 목록 조회 테스트")
    void findAllTest(){
        // given
        saveCart(2, product);
        saveCart(1, product2);
        saveCart(5, product3);

        // when
        List<CartDto> carts = cartService.findAllByUser(userId);

        // then
        assertThat(carts).hasSize(3);
    }

    @Test
    @DisplayName("장바구니 목록 제거 테스트")
    void deleteTest(){
        // given
        Cart cart = saveCart(2, product);
        saveCart(1, product2);

        // when
        cartService.delete(userId, cart.getId());

        // then
        List<CartDto> carts = cartService.findAllByUser(userId);
        assertThat(carts).hasSize(1);
    }

    private Cart saveCart(int quantity, Product product) {
        return cartRepository.save(new Cart(quantity, product, userId));
    }
}