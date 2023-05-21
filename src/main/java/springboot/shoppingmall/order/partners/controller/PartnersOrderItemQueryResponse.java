package springboot.shoppingmall.order.partners.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnersOrderItemQueryResponse {
    private Long id;
    private String productCode;
    private String productName;
    private int quantity;
    private String invoiceNumber;

    public static PartnersOrderItemQueryResponse to(PartnersOrderItemQueryDto dto) {
        return new PartnersOrderItemQueryResponse(
                dto.getId(), dto.getProductCode(), dto.getProductName(),
                dto.getQuantity(), dto.getInvoiceNumber()
        );
    }
}
