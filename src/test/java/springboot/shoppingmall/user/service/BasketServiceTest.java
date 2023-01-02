package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
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
import springboot.shoppingmall.user.domain.Basket;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.BasketRequest;
import springboot.shoppingmall.user.dto.BasketResponse;

@Transactional
@SpringBootTest
class BasketServiceTest {

    @Autowired
    BasketService basketService;

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
    void createBasketTest(){
        // given
        BasketRequest basketRequest = new BasketRequest(2, product.getId());

        // when
        BasketResponse basketResponse = basketService.create(saveUser.getId(), basketRequest);

        // then
        assertThat(basketResponse.getId()).isNotNull();
        assertThat(basketResponse.getQuantity()).isEqualTo(2);
        assertThat(basketResponse.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("장바구니 목록 조회 테스트")
    void findAllTest(){
        // given
        basketService.create(saveUser.getId(), new BasketRequest(2, product.getId()));
        basketService.create(saveUser.getId(), new BasketRequest(1, product2.getId()));
        basketService.create(saveUser.getId(), new BasketRequest(5, product3.getId()));

        // when
        List<BasketResponse> baskets = basketService.findAllByUser(saveUser.getId());

        // then
        assertThat(baskets).hasSize(3);
    }

    @Test
    @DisplayName("장바구니 목록 제거 테스트")
    void deleteTest(){
        // given
        BasketResponse basketResponse = basketService.create(saveUser.getId(), new BasketRequest(2, product.getId()));
        basketService.create(saveUser.getId(), new BasketRequest(1, product2.getId()));

        // when
        basketService.delete(saveUser.getId(), basketResponse.getId());

        // then
        assertThat(saveUser.getBaskets()).hasSize(1);
    }

}