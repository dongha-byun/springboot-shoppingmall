package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryInvoiceResponse {
    private String invoiceNumber;

    private String receiverName;
    private String receiverZipcode;
    private String receiverAddress;
    private String receiverDetailAddress;

    private String providerName;
    private String providerZipcode;
    private String providerAddress;
    private String providerDetailAddress;
}
