package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.user.dto.DeliveryResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {
    private Long id;
    private String orderStatusName;
    private String productName;
    private int quantity;
    private int totalPrice;
    private DeliveryResponse delivery;

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus().getStatusName(),
                order.getProduct().getName(), order.getQuantity(),
                order.getTotalPrice(), DeliveryResponse.of(order.getDelivery()));
    }
}
