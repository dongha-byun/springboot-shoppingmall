package springboot.shoppingmall.order.partners.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnersOrderItemQueryDto {
    private Long id;
    private Long orderId;
    private String productName;
    private String productCode;
    private int quantity;
    private String invoiceNumber;
}
