package springboot.shoppingmall.cart.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.cart.domain.Cart;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private ProductResponse product;
    private int quantity;

    public static CartResponse of(Cart saveCart) {
        return new CartResponse(
                saveCart.getId(),
                null,
                saveCart.getQuantity()
        );
    }
}
