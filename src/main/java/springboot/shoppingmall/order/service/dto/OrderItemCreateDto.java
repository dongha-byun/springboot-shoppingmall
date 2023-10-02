package springboot.shoppingmall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemCreateDto {
    private Long productId;
    private int quantity;
    private Long usedCouponId;
}
