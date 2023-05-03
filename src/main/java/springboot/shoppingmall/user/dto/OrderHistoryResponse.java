package springboot.shoppingmall.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String orderStatusName;
    private Long productId;
    private String productName;
    private int orderPrice;
    private Long partnerId;
    private String partnerName;

    public static OrderHistoryResponse to(OrderHistoryDto dto) {
        return new OrderHistoryResponse(
                dto.getOrderId(), dto.getOrderDate(), dto.getOrderStatus(), dto.getOrderStatusName(),
                dto.getProductId(), dto.getProductName(), dto.getOrderPrice(),
                dto.getProviderId(), dto.getProviderName()
        );
    }
}
