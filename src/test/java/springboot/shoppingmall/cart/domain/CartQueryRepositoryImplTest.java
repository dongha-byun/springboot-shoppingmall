package springboot.shoppingmall.cart.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.partners.domain.PartnerRepository;

@Transactional
@SpringBootTest
class CartQueryRepositoryImplTest {

    @Autowired
    CartQueryRepository cartQueryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Long userId = 100L;
    Category category;
    Category subCategory;
    Product product1;
    Cart cart1;
    Partner partner;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(new Category("상위 카테고리 1"));
        subCategory = categoryRepository.save(new Category("하위 카테고리 1").changeParent(category));
        partner = partnerRepository.save(new Partner(
                "(주)파산은행", "김사채", "110-44-66666", "070-4444-4989",
                "서울시 사채구 빚더미동", "cash_bank", "1q2w3e4r!", true
        ));

        product1 = productRepository.save(
                new Product("상품 1", 12000, 10, category, subCategory
                        , partner.getId(), "stored1", "real2", "상품 설명 입니다.", "test-product-code")
        );

        cart1 = cartRepository.save(new Cart(3, product1.getId(), userId));
    }

    @Test
    @DisplayName("사용자 장바구니 dto 항목 점검 테스트")
    void find_cart_dto(){
        // given

        // when
        List<CartQueryDto> carts = cartQueryRepository.findAllCartByUserId(userId);

        // then
        assertThat(carts).hasSize(1)
                .extracting("id", "productName", "quantity", "productImageFileName")
                .containsExactly(
                        tuple(cart1.getId(), "상품 1", 3, "stored1")
                );

    }

    @Test
    @DisplayName("사용자 장바구니 목록 조회")
    void find_all_cart_dto() {
        // given
        Product product2 = productRepository.save(
                new Product("상품 2", 13000, 5, category, subCategory,
                        partner.getId(), "stored2", "real2", "상품 설명 입니다.",
                        "test-product-code")
        );
        Cart cart2 = cartRepository.save(new Cart(5, product2.getId(), userId));

        // when
        List<CartQueryDto> carts = cartQueryRepository.findAllCartByUserId(userId);

        // then
        assertThat(carts).hasSize(2)
                .extracting("id", "partnersName", "partnersId", "productImageFileName")
                .containsExactly(
                        tuple(cart1.getId(), "(주)파산은행", partner.getId(), "stored1"),
                        tuple(cart2.getId(), "(주)파산은행", partner.getId(), "stored2")
                );
    }

}