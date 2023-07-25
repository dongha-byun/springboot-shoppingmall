package springboot.shoppingmall.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {
    private Long id;
    private int quantity;
    private Long productId;
    private String productName;
    private int price;
    private Long partnersId;
    private String partnersName;
    private String storedImgFileName;
}
