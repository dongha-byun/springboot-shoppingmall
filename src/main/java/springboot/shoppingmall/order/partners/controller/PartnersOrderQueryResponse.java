package springboot.shoppingmall.order.partners.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryResponse {
    private Long orderItemId;
    private String orderCode;
    private String orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private String invoiceNumber;
    private int totalPrice;
    private String userName;
    private String userTelNo;
    private String orderStatusName;
}
