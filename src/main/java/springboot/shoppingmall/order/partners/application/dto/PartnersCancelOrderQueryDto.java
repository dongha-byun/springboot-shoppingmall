package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.domain.OrderStatus;

@NoArgsConstructor
@Getter
@Setter
public class PartnersCancelOrderQueryDto extends PartnersOrderQueryDto{
    private LocalDateTime cancelDate;
    private String cancelReason;
    private LocalDateTime refundDate;
    private String refundReason;
    private LocalDateTime exchangeDate;
    private String exchangeReason;

    public PartnersCancelOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate, String productCode,
                                       String productName, int quantity, String invoiceNumber, int totalPrice,
                                       String userName, String userTelNo, OrderStatus orderStatus,
                                       LocalDateTime cancelDate,
                                       String cancelReason, LocalDateTime refundDate, String refundReason,
                                       LocalDateTime exchangeDate, String exchangeReason) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userName,
                userTelNo, orderStatus);
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
        this.exchangeDate = exchangeDate;
        this.exchangeReason = exchangeReason;
    }
}
