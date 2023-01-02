package springboot.shoppingmall.user.dto;

import javax.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.user.domain.Basket;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponse {
    private Long id;
    private ProductResponse product;
    private int quantity;

    public static BasketResponse of(Basket saveBasket) {
        return new BasketResponse(
                saveBasket.getId(),
                ProductResponse.of(saveBasket.getProduct()),
                saveBasket.getQuantity()
        );
    }
}
