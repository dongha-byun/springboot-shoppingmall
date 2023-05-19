package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.product.domain.Product;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;

    public static OrderItemResponse of(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        return new OrderItemResponse(orderItem.getId(), product.getId(),
                product.getName(), orderItem.getQuantity());
    }
}
