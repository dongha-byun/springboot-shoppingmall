package springboot.shoppingmall.cart.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.domain.Cart;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;

    public static CartResponse of(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getProduct().getId(),
                cart.getProduct().getName(),
                cart.getProduct().getPrice(),
                cart.getQuantity()
        );
    }
}
