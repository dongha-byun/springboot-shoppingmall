package springboot.shoppingmall.cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class CartQueryRepositoryImplTest extends IntegrationTest {

    @Autowired
    CartQueryRepository cartQueryRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Long userId = 100L;
    Product product1, product2;
    Cart cart1;
    Partner partner;

    @BeforeEach
    void setup() {
        Long categoryId = 1L;
        Long subCategoryId = 11L;
        partner = partnerRepository.save(new Partner(
                "(주)파산은행", "김사채", "110-44-66666", "070-4444-4989",
                "서울시 사채구 빚더미동", "cash_bank", "1q2w3e4r!", true
        ));

        product1 = saveProduct(
                "상품 1", 12000, 10, 1.9, 112,
                categoryId, subCategoryId, partner.getId(), LocalDateTime.now()
        );
        product2 = saveProduct(
                "상품 2", 13000, 5, 3.9, 313,
                categoryId, subCategoryId, partner.getId(), LocalDateTime.now()
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
                .extracting("id", "productName", "quantity")
                .containsExactly(
                        tuple(cart1.getId(), "상품 1", 3)
                );

    }

    @Test
    @DisplayName("사용자 장바구니 목록 조회")
    void find_all_cart_dto() {
        // given
        Cart cart2 = cartRepository.save(new Cart(5, product2.getId(), userId));

        // when
        List<CartQueryDto> carts = cartQueryRepository.findAllCartByUserId(userId);

        // then
        assertThat(carts).hasSize(2)
                .extracting("id", "partnersName", "partnersId")
                .containsExactly(
                        tuple(cart1.getId(), "(주)파산은행", partner.getId()),
                        tuple(cart2.getId(), "(주)파산은행", partner.getId())
                );
    }

}