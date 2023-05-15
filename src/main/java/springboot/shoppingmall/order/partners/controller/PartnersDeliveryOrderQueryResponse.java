package springboot.shoppingmall.order.partners.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Data
public class PartnersDeliveryOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String invoiceNumber;

    public PartnersDeliveryOrderQueryResponse(Long id, String orderCode, String orderDate,
                                              String productCode, String productName, int quantity, int totalPrice,
                                              String userName, String userTelNo, String orderStatusName,
                                              String receiverName, String address, String detailAddress,
                                              String requestMessage, String invoiceNumber) {
        super(id, orderCode, orderDate, productCode, productName, quantity, totalPrice, userName, userTelNo,
                orderStatusName);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.invoiceNumber = invoiceNumber;
    }

    public static PartnersDeliveryOrderQueryResponse to(PartnersDeliveryOrderQueryDto dto) {
        return new PartnersDeliveryOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getTotalPrice(),
                dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage(), dto.getInvoiceNumber()
        );
    }
}
