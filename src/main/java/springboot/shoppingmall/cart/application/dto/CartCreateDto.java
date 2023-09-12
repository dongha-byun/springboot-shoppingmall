package springboot.shoppingmall.cart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartCreateDto {
    private int quantity;
    private Long productId;
}
