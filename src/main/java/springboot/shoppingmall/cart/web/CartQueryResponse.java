package springboot.shoppingmall.cart.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.dto.CartDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartQueryResponse {
    private Long id;
    private int quantity;
    private Long productId;
    private String productName;
    private int price;
    private String partnersName;
    private String storedImgFileName;

    public static CartQueryResponse of(CartDto cartDto) {
        return new CartQueryResponse(
                cartDto.getId(), cartDto.getQuantity(), cartDto.getProductId(),
                cartDto.getProductName(), cartDto.getPrice(), cartDto.getPartnersName(),
                cartDto.getStoredImgFileName()
        );
    }
}
