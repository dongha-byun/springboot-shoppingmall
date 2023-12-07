package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartnersCancelOrderQueryDto {
    private Long orderItemId;
    private Long orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private String invoiceNumber;
    private int totalPrice;
    private Long userId;
    private String userName;
    private String userTelNo;
    private OrderStatus orderStatus;
    private OrderItemResolutionType resolutionType;
    private LocalDateTime resolutionDate;
    private String resolutionReason;

    public PartnersCancelOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                       String productCode, String productName, int quantity, String invoiceNumber,
                                       int totalPrice, Long userId, OrderStatus orderStatus,
                                       OrderItemResolutionType resolutionType, LocalDateTime resolutionDate, String resolutionReason) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.invoiceNumber = invoiceNumber;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.resolutionType = resolutionType;
        this.resolutionDate = resolutionDate;
        this.resolutionReason = resolutionReason;
    }
}
