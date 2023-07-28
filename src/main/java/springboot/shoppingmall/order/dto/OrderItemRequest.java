package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private Long userCouponId;

    public OrderItemRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
