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
    private String productName;
    private int price;
    private Long partnersId;
    private String partnersName;
    private String storedImgFileName;

    public static CartDto of(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .productId(cart.getProduct().getId())
                .productName(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .partnersId(cart.getProduct().getPartnerId())
                .storedImgFileName(cart.getProduct().getThumbnail())
                .build();
    }
}
