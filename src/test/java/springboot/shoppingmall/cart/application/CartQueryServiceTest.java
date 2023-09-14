package springboot.shoppingmall.cart.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.application.dto.CartDto;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;

@Transactional
@SpringBootTest
class CartQueryServiceTest {

    @Autowired
    CartQueryService cartQueryService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProductRepository productRepository;

    Product product, product2, product3;

    Long userId = 1L;

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
    @DisplayName("사용자의 장바구니 목록을 조회한다.")
    void find_all_carts_by_user() {
        // given
        Cart cart = saveCart(2, product);
        Cart cart2 = saveCart(1, product2);
        Cart cart3 = saveCart(5, product3);

        // when
        List<CartQueryDto> carts = cartQueryService.findAllByUser(userId);

        // then
        assertThat(carts).hasSize(3)
                .extracting("id", "quantity", "productId")
                .containsExactly(
                        tuple(cart.getId(), 2, product.getId()),
                        tuple(cart2.getId(), 1, product2.getId()),
                        tuple(cart3.getId(), 5, product3.getId())
                );
    }

    private Cart saveCart(int quantity, Product product) {
        return cartRepository.save(new Cart(quantity, product.getId(), userId));
    }
}