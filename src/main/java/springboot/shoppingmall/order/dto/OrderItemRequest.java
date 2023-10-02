package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.service.dto.OrderItemCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private Long usedCouponId;

    public OrderItemCreateDto toDto() {
        return new OrderItemCreateDto(productId, quantity, usedCouponId);
    }
}
