package springboot.shoppingmall.deliveryinvoice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryInvoiceResponse {
    private String invoiceNumber;

    private String receiverName;
    private String receiverZipcode;
    private String receiverAddress;
    private String receiverDetailAddress;

    private String providerName;
    private String providerZipcode;
    private String providerAddress;
    private String providerDetailAddress;

    @Builder
    public DeliveryInvoiceResponse(String invoiceNumber, String receiverName, String receiverZipcode,
                                   String receiverAddress, String receiverDetailAddress, String providerName,
                                   String providerZipcode, String providerAddress, String providerDetailAddress) {
        this.invoiceNumber = invoiceNumber;
        this.receiverName = receiverName;
        this.receiverZipcode = receiverZipcode;
        this.receiverAddress = receiverAddress;
        this.receiverDetailAddress = receiverDetailAddress;
        this.providerName = providerName;
        this.providerZipcode = providerZipcode;
        this.providerAddress = providerAddress;
        this.providerDetailAddress = providerDetailAddress;
    }

    public static DeliveryInvoiceResponse of(String invoiceNumber, DeliveryInvoiceRequest request){
        return DeliveryInvoiceResponse.builder()
                .invoiceNumber(invoiceNumber)
                .receiverName(request.getReceiverName())
                .receiverZipcode(request.getReceiverZipcode())
                .receiverAddress(request.getReceiverAddress())
                .receiverDetailAddress(request.getReceiverDetailAddress())
                .providerName(request.getProviderName())
                .providerZipcode(request.getProviderZipcode())
                .providerAddress(request.getProviderAddress())
                .providerDetailAddress(request.getProviderDetailAddress())
                .build();
    }
}
