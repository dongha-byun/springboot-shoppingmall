package springboot.shoppingmall.cart.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.application.dto.CartDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long productId;
    private int quantity;

    public static CartResponse of(CartDto cartDto) {
        return new CartResponse(
                cartDto.getId(),
                cartDto.getProductId(),
                cartDto.getQuantity()
        );
    }
}
