package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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
    private String userName;
    private String userTelNo;
    private OrderStatus orderStatus;
    private LocalDateTime cancelDate;   // 취소일자
    private String cancelReason;        // 취소사유
    private LocalDateTime refundDate;   // 환불일자
    private String refundReason;        // 환불사유
    private LocalDateTime exchangeDate; // 교환일자
    private String exchangeReason;      // 교환사유
}
