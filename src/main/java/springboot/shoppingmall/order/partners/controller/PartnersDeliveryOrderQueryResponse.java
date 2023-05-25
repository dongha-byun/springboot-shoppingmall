package springboot.shoppingmall.order.partners.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Getter
public class PartnersDeliveryOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public PartnersDeliveryOrderQueryResponse(Long orderItemId, String orderCode, String orderDate, String productCode,
                                              String productName, int quantity, String invoiceNumber, int totalPrice,
                                              String userName, String userTelNo, String orderStatusName,
                                              String receiverName, String address, String detailAddress,
                                              String requestMessage) {
        super(orderItemId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userName,
                userTelNo, orderStatusName);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }

    public static PartnersDeliveryOrderQueryResponse to(PartnersDeliveryOrderQueryDto dto) {
        return new PartnersDeliveryOrderQueryResponse(
                dto.getOrderItemId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getInvoiceNumber(),
                dto.getTotalPrice(), dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage()
        );
    }
}