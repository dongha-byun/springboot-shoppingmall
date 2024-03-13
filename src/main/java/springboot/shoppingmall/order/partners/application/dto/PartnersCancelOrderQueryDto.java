package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderStatus;

@NoArgsConstructor
@Getter
@Setter
public class PartnersCancelOrderQueryDto extends PartnersOrderQueryDto{
    private OrderItemResolutionType resolutionType;
    private LocalDateTime resolutionDate;
    private String resolutionReason;

    public PartnersCancelOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                       String productCode, String productName, int quantity, String invoiceNumber,
                                       int totalPrice, Long userId, String userName, String userTelNo,
                                       OrderStatus orderStatus, OrderItemResolutionType resolutionType,
                                       LocalDateTime resolutionDate, String resolutionReason) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userId, userName, userTelNo, orderStatus);
        this.resolutionType = resolutionType;
        this.resolutionDate = resolutionDate;
        this.resolutionReason = resolutionReason;
    }

    public PartnersCancelOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                       String productCode, String productName, int quantity, String invoiceNumber,
                                       int totalPrice, Long userId,
                                       OrderStatus orderStatus, OrderItemResolutionType resolutionType,
                                       LocalDateTime resolutionDate, String resolutionReason) {
        this(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber,totalPrice,
                userId, null, null, orderStatus, resolutionType, resolutionDate, resolutionReason);
    }
}
