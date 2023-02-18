package springboot.shoppingmall.cart.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.cart.web.CartRequest;
import springboot.shoppingmall.cart.web.CartResponse;

@Transactional
@SpringBootTest
class CartServiceTest {

    @Autowired
    CartService cartService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    Product product2;
    Product product3;
    User saveUser;

    @BeforeEach
    void setUp(){
        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(new Product("상품 1", 22000, 10, category, subCategory));
        product2 = productRepository.save(new Product("상품 2", 32000, 5, category, subCategory));
        product3 = productRepository.save(new Product("상품 3", 42000, 3, category, subCategory));
        saveUser = userRepository.save(User.builder()
                .userName("테스터1")
                .loginId("tester1")
                .password("tester1!")
                .telNo("010-2222-3333")
                .build());
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void createCartTest(){
        // given
        CartRequest cartRequest = new CartRequest(2, product.getId());

        // when
        CartResponse cartResponse = cartService.create(saveUser.getId(), cartRequest);

        // then
        assertThat(cartResponse.getId()).isNotNull();
        assertThat(cartResponse.getQuantity()).isEqualTo(2);
        assertThat(cartResponse.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("장바구니 목록 조회 테스트")
    void findAllTest(){
        // given
        cartService.create(saveUser.getId(), new CartRequest(2, product.getId()));
        cartService.create(saveUser.getId(), new CartRequest(1, product2.getId()));
        cartService.create(saveUser.getId(), new CartRequest(5, product3.getId()));

        // when
        List<CartResponse> carts = cartService.findAllByUser(saveUser.getId());

        // then
        assertThat(carts).hasSize(3);
    }

    @Test
    @DisplayName("장바구니 목록 제거 테스트")
    void deleteTest(){
        // given
        CartResponse cartResponse = cartService.create(saveUser.getId(), new CartRequest(2, product.getId()));
        cartService.create(saveUser.getId(), new CartRequest(1, product2.getId()));

        // when
        cartService.delete(saveUser.getId(), cartResponse.getId());

        // then
        assertThat(saveUser.getBaskets()).hasSize(1);
    }

}