package springboot.shoppingmall.cart.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.dto.CartDto;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;
import springboot.shoppingmall.cart.web.CartRequest;
import springboot.shoppingmall.cart.web.CartResponse;

@Transactional
@SpringBootTest
class CartServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    CartService cartService;

    @Autowired
    ProviderRepository providerRepository;

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

        Provider provider = providerRepository.save(
                new Provider("단절통신", "통신사대표", "서울시 영등포구 여의도2동", "02-3331-2321",
                        "110-22-331223", "cut_communication", "cut_communication1!")
        );

        product = productRepository.save(
                new Product("상품 1", 22000, 10, category, subCategory,
                        provider.getId(), "product_stored_file_name1", "product_view_file_name1",
                        "상품 설명 입니다.", "test-product-code")
        );
        product2 = productRepository.save(
                new Product("상품 2", 32000, 5, category, subCategory,
                        provider.getId(), "product_stored_file_name1", "product_view_file_name1",
                        "상품 설명 입니다.", "test-product-code")
        );
        product3 = productRepository.save(
                new Product("상품 3", 42000, 3, category, subCategory,
                        provider.getId(), "product_stored_file_name1", "product_view_file_name1",
                        "상품 설명 입니다.", "test-product-code")
        );
        saveUser = userRepository.save(User.builder()
                .userName("테스터1")
                .email("tester1@test.com")
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
        assertThat(cartResponse.getProductId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("장바구니 목록 조회 테스트")
    void findAllTest(){
        // given
        cartService.create(saveUser.getId(), new CartRequest(2, product.getId()));
        cartService.create(saveUser.getId(), new CartRequest(1, product2.getId()));
        cartService.create(saveUser.getId(), new CartRequest(5, product3.getId()));

        em.flush();
        em.clear();
        // when
        List<CartDto> carts = cartService.findAllByUser(saveUser.getId());

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

        em.flush();
        em.clear();

        // then
        List<CartDto> carts = cartService.findAllByUser(saveUser.getId());
        assertThat(carts).hasSize(1);
    }

}