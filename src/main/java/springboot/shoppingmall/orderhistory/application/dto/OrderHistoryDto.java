package springboot.shoppingmall.orderhistory.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@Getter
@NoArgsConstructor
public class OrderHistoryDto {
    private Long orderId;
    private Long orderItemId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String orderStatusName;
    private Long productId;
    private String productName;
    private String tid;
    private int orderPrice;
    private Long providerId;
    private String providerName;

    @QueryProjection
    public OrderHistoryDto(Long orderId, Long orderItemId, LocalDateTime orderDate, OrderStatus orderStatus,
                           Long productId, String productName, String tid, int orderPrice,
                           Long providerId, String providerName) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatus.getStatusName();
        this.productId = productId;
        this.productName = productName;
        this.tid = tid;
        this.orderPrice = orderPrice;
        this.providerId = providerId;
        this.providerName = providerName;
    }
}
