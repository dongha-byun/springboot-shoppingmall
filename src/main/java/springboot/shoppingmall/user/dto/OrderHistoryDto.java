package springboot.shoppingmall.user.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@Getter
@NoArgsConstructor
public class OrderHistoryDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String orderStatusName;
    private String productName;
    private int orderPrice;

    public OrderHistoryDto(Long orderId, LocalDateTime orderDate, OrderStatus orderStatus,
                           String productName, int orderPrice) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatus.getStatusName();
        this.productName = productName;
        this.orderPrice = orderPrice;
    }
}
