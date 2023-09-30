package springboot.shoppingmall.order.partners.presentation.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Getter
@Setter
public class PartnersEndOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String receiverPhoneNumber;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String deliveryDate;
    private String deliveryPlace;

    public PartnersEndOrderQueryResponse(Long orderItemId, Long orderId, String orderCode, String orderDate, String productCode,
                                         String productName, int quantity, String invoiceNumber, int totalPrice,
                                         String userName, String userTelNo, String orderStatusName,
                                         String receiverName, String receiverPhoneNumber,
                                         String address, String detailAddress, String requestMessage,
                                         String deliveryDate, String deliveryPlace) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userName, userTelNo, orderStatusName);
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.deliveryDate = deliveryDate;
        this.deliveryPlace = deliveryPlace;
    }

    public static PartnersEndOrderQueryResponse of(PartnersEndOrderQueryDto dto) {
        return new PartnersEndOrderQueryResponse(
                dto.getOrderItemId(), dto.getOrderId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getInvoiceNumber(),
                dto.getTotalPrice(), dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getReceiverPhoneNumber(),
                dto.getAddress(), dto.getDetailAddress(), dto.getRequestMessage(),
                DateUtils.toStringOfLocalDateTIme(dto.getDeliveryDate()), dto.getDeliveryPlace()
        );
    }
}
