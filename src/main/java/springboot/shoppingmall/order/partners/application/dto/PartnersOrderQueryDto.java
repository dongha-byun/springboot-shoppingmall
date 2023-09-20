package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryDto {
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
}