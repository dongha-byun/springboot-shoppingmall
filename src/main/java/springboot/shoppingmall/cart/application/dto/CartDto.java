package springboot.shoppingmall.cart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.domain.Cart;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartDto {
    private Long id;
    private int quantity;
    private Long productId;

    public static CartDto of(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .productId(cart.getProductId())
                .build();
    }
}
