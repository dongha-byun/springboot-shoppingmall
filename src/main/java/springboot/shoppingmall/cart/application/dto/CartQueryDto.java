package springboot.shoppingmall.cart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartQueryDto {
    private Long id;
    private int quantity;
    private Long productId;
    private String productName;
    private int productPrice;
    private Long partnersId;
    private String partnersName;
    private String productImageFileName;
}
