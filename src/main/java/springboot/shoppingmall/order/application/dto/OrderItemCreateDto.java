package springboot.shoppingmall.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemCreateDto {
    private Long productId;
    private int quantity;
    private Long usedCouponId;
}
