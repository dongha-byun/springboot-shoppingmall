package springboot.shoppingmall.cart.application;

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
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class CartQueryServiceTest extends IntegrationTest {

    @Autowired
    CartQueryService cartQueryService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Product product, product2, product3;

    Long userId = 1L;

    @BeforeEach
    void setUp(){
        Long categoryId = 1L;
        Long subCategoryId = 11L;

        Partner partner = partnerRepository.save(
                new Partner("판매업체", "판매대표", "판매업체 주소", "031-222-3311", "222-33-112233", "test1", "test1!")
        );

        LocalDateTime regDate = LocalDateTime.of(2023, 12, 11, 12, 33, 12);
        product = saveProduct(
                "상품 1", 22000, 10, 1.0, 123,
                categoryId, subCategoryId, partner.getId(), regDate
        );
        product2 = saveProduct(
                "상품 2", 32000, 5, 4.0, 1232,
                categoryId, subCategoryId, partner.getId(), regDate
        );
        product3 = saveProduct(
                "상품 3", 42000, 3, 2.9, 784,
                categoryId, subCategoryId, partner.getId(), regDate
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