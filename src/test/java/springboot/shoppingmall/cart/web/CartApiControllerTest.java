package springboot.shoppingmall.cart.web;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.cart.dto.CartDto;
import springboot.shoppingmall.cart.service.CartService;

class CartApiControllerTest {

    @DisplayName("나의 장바구니 목록을 조회한다.")
    @Test
    void find_my_carts() throws Exception {
        // given
        CartService mockCartService = mock(CartService.class);
        CartApiController cartApiController = new CartApiController(mockCartService);

        List<CartDto> carts = Arrays.asList(
                new CartDto(1L, 3, 10L, "상품1", 1200, 100L, "상품판매자1", "product1"),
                new CartDto(2L, 4, 20L, "상품2", 1700, 100L, "상품판매자2", "product2"),
                new CartDto(3L, 5, 30L, "상품3", 2200, 100L, "상품판매자3", "product3")
        );
        when(mockCartService.findAllByUser(anyLong()))
                .thenReturn(carts);

        // when
        ResponseEntity<List<CartQueryResponse>> response = cartApiController.findAllCarts(
                new AuthorizedUser(1L)
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CartQueryResponse> body = response.getBody();
        assertThat(body).hasSize(3)
                .extracting("id", "productId", "partnersId")
                .containsExactly(
                        tuple(1L, 10L, 100L),
                        tuple(2L, 20L, 100L),
                        tuple(3L, 30L, 100L)
                );
    }

}