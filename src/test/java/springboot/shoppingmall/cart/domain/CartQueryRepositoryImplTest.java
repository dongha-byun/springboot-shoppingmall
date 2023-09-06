package springboot.shoppingmall.cart.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
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

@Transactional
@SpringBootTest
class CartQueryRepositoryImplTest {

    @Autowired
    CartQueryRepository cartQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProviderRepository providerRepository;

    User user1;
    Category category;
    Category subCategory;
    Product product1;
    Cart cart1;
    Provider provider;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(new User(
                "테스터", "tester", "tester1!", "010-2222-3333"
        ));
        category = categoryRepository.save(new Category("상위 카테고리 1"));
        subCategory = categoryRepository.save(new Category("하위 카테고리 1").changeParent(category));
        provider = providerRepository.save(new Provider(
                "(주)파산은행", "김사채", "110-44-66666", "070-4444-4989",
                "서울시 사채구 빚더미동", "cash_bank", "1q2w3e4r!", true
        ));

        product1 = productRepository.save(
                new Product("상품 1", 12000, 10, category, subCategory
                        , provider.getId(), "stored1", "real2", "상품 설명 입니다.", "test-product-code")
        );

        cart1 = cartRepository.save(new Cart(3, product1, user1));
    }

    @Test
    @DisplayName("사용자 장바구니 dto 항목 점검 테스트")
    void find_cart_dto(){
        // given

        // when
        List<CartDto> cartDtos = cartQueryRepository.findAllCartByUserId(user1.getId());
        CartDto cartDto = cartDtos.get(0);

        // then
        assertThat(cartDto).isNotNull();
        assertThat(cartDto.getId()).isEqualTo(cart1.getId());
        assertThat(cartDto.getProductId()).isEqualTo(cart1.getProduct().getId());
        assertThat(cartDto.getProductName()).isEqualTo(cart1.getProduct().getName());
        assertThat(cartDto.getQuantity()).isEqualTo(cart1.getQuantity());
        assertThat(cartDto.getPrice()).isEqualTo(cart1.getProduct().getPrice());
        assertThat(cartDto.getPartnersId()).isEqualTo(provider.getId());
        assertThat(cartDto.getPartnersName()).isEqualTo(provider.getName());
        assertThat(cartDto.getStoredImgFileName()).isEqualTo(product1.getThumbnail());
    }

    @Test
    @DisplayName("사용자 장바구니 목록 조회")
    void find_all_cart_dto() {
        // given
        Product product2 = productRepository.save(
                new Product("상품 2", 13000, 5, category, subCategory,
                        provider.getId(), "stored2", "real2", "상품 설명 입니다.",
                        "test-product-code")
        );
        Cart cart2 = cartRepository.save(new Cart(5, product2, user1));

        // when
        List<CartDto> cartDtos = cartQueryRepository.findAllCartByUserId(user1.getId());

        // then
        assertThat(cartDtos).hasSize(2)
                .extracting("id", "partnersName", "partnersId", "storedImgFileName")
                .containsExactly(
                        tuple(cart1.getId(), "(주)파산은행", provider.getId(), "stored1"),
                        tuple(cart2.getId(), "(주)파산은행", provider.getId(), "stored2")
                );
    }

}