package springboot.shoppingmall.order.partners.controller;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.partners.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Getter
@Setter
public class PartnersEndOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String invoiceNumber;
    private String deliveryDate;
    private String deliveryPlace;

    public PartnersEndOrderQueryResponse(Long id, String orderCode, String orderDate, String productCode,
                                         String productName, int quantity, int totalPrice, String userName,
                                         String userTelNo, String orderStatusName, String receiverName, String address,
                                         String detailAddress, String requestMessage, String invoiceNumber,
                                         String deliveryDate, String deliveryPlace) {
        super(id, orderCode, orderDate, productCode, productName, quantity, totalPrice, userName, userTelNo,
                orderStatusName);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.invoiceNumber = invoiceNumber;
        this.deliveryDate = deliveryDate;
        this.deliveryPlace = deliveryPlace;
    }

    public static PartnersEndOrderQueryResponse to(PartnersEndOrderQueryDto dto) {
        return new PartnersEndOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getTotalPrice(),
                dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage(), dto.getInvoiceNumber(),
                DateUtils.toStringOfLocalDateTIme(dto.getDeliveryDate()), dto.getDeliveryPlace()
        );
    }
}
