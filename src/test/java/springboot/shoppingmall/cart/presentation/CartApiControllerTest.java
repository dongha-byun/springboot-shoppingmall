package springboot.shoppingmall.cart.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.cart.application.CartQueryService;

import springboot.shoppingmall.cart.application.CartService;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.presentation.response.CartQueryResponse;

class CartApiControllerTest {

    @DisplayName("나의 장바구니 목록을 조회한다.")
    @Test
    void find_my_carts() throws Exception {
        // given
        CartService mockCartService = mock(CartService.class);
        CartQueryService mockCartQueryService = mock(CartQueryService.class);
        CartApiController cartApiController = new CartApiController(mockCartService, mockCartQueryService);

        List<CartQueryDto> carts = Arrays.asList(
                new CartQueryDto(1L, 3, 10L, "상품1", 1200, 100L, "상품판매자1", "product1"),
                new CartQueryDto(2L, 4, 20L, "상품2", 1700, 100L, "상품판매자2", "product2"),
                new CartQueryDto(3L, 5, 30L, "상품3", 2200, 100L, "상품판매자3", "product3")
        );
        when(mockCartQueryService.findAllByUser(anyLong())).thenReturn(carts);

        // when
        ResponseEntity<List<CartQueryResponse>> response = cartApiController.findAllCarts(
                new GatewayAuthInfo(1L)
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