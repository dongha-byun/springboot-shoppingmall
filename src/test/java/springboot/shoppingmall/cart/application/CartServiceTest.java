package springboot.shoppingmall.cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.cart.application.dto.CartCreateDto;
import springboot.shoppingmall.cart.application.dto.CartDto;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.domain.Cart;
import springboot.shoppingmall.cart.domain.CartRepository;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class CartServiceTest extends IntegrationTest {

    @Autowired
    CartService cartService;

    @Autowired
    CartQueryService cartQueryService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Product product, product2, product3;
    Long userId = 10L;

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
    @DisplayName("장바구니에 상품을 추가한다.")
    void create(){
        // given
        CartCreateDto cartCreateDto = new CartCreateDto(2, product.getId());

        // when
        CartDto cartDto = cartService.create(userId, cartCreateDto);

        // then
        assertThat(cartDto.getId()).isNotNull();
        assertThat(cartDto.getQuantity()).isEqualTo(2);
        assertThat(cartDto.getProductId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("장바구니에 담은 상품을 제거한다.")
    void delete(){
        // given
        Cart cart = saveCart(2, product);
        saveCart(1, product2);

        // when
        cartService.delete(userId, cart.getId());

        // then
        List<CartQueryDto> carts = cartQueryService.findAllByUser(userId);
        assertThat(carts).hasSize(1);
    }

    private Cart saveCart(int quantity, Product product) {
        return cartRepository.save(new Cart(quantity, product.getId(), userId));
    }
}