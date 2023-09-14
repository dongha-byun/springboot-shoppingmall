package springboot.shoppingmall.cart.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartQueryResponse {
    private Long id;
    private int quantity;
    private Long productId;
    private String productName;
    private int price;
    private Long partnersId;
    private String partnersName;
    private String storedImgFileName;

    public static CartQueryResponse of(CartQueryDto cartDto) {
        return new CartQueryResponse(
                cartDto.getId(), cartDto.getQuantity(), cartDto.getProductId(),
                cartDto.getProductName(), cartDto.getProductPrice(),
                cartDto.getPartnersId(), cartDto.getPartnersName(),
                cartDto.getProductImageFileName()
        );
    }
}
