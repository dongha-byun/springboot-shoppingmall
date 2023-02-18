package springboot.shoppingmall.cart.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartRequest {
    private int quantity;
    private Long productId;
}
