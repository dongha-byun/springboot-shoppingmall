package springboot.shoppingmall.orderhistory.presentation.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryResponse {
    private Long orderId;
    private Long orderItemId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String orderStatusName;
    private Long productId;
    private String productName;
    private String tid;
    private int orderPrice;
    private Long partnerId;
    private String partnerName;

    public static OrderHistoryResponse to(OrderHistoryDto dto) {
        return new OrderHistoryResponse(
                dto.getOrderId(), dto.getOrderItemId(), dto.getOrderDate(),
                dto.getOrderStatus(), dto.getOrderStatusName(),
                dto.getProductId(), dto.getProductName(), dto.getTid(), dto.getOrderPrice(),
                dto.getProviderId(), dto.getProviderName()
        );
    }
}
