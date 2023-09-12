package springboot.shoppingmall.cart.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.application.dto.CartCreateDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartRequest {
    private int quantity;
    private Long productId;

    public CartCreateDto toDto() {
        return CartCreateDto.builder()
                .quantity(this.quantity)
                .productId(this.productId)
                .build();
    }
}
