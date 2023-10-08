package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private LocalDateTime cancelDate;   // 취소일자
    private String cancelReason;        // 취소사유
    private LocalDateTime refundDate;   // 환불일자
    private String refundReason;        // 환불사유
    private LocalDateTime exchangeDate; // 교환일자
    private String exchangeReason;      // 교환사유

    public PartnersCancelOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                       String productCode, String productName, int quantity, String invoiceNumber,
                                       int totalPrice, Long userId, OrderStatus orderStatus, LocalDateTime cancelDate,
                                       String cancelReason, LocalDateTime refundDate, String refundReason,
                                       LocalDateTime exchangeDate, String exchangeReason) {
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
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
        this.exchangeDate = exchangeDate;
        this.exchangeReason = exchangeReason;
    }
}
