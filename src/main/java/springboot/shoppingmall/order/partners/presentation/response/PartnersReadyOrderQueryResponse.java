package springboot.shoppingmall.order.partners.presentation.response;

import static springboot.shoppingmall.utils.DateUtils.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;

@NoArgsConstructor
@Getter
public class PartnersReadyOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String receiverPhoneNumber;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public PartnersReadyOrderQueryResponse(Long orderItemId, Long orderId, String orderCode, String orderDate, String productCode,
                                           String productName, int quantity, String invoiceNumber, int totalPrice,
                                           String userName, String userTelNo, String orderStatusName,
                                           String receiverName, String receiverPhoneNumber,
                                           String address, String detailAddress, String requestMessage) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName,
                quantity, invoiceNumber, totalPrice, userName, userTelNo, orderStatusName);
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }

    public static PartnersReadyOrderQueryResponse of(PartnersReadyOrderQueryDto dto) {
        return new PartnersReadyOrderQueryResponse(
                dto.getOrderItemId(), dto.getOrderId(), dto.getOrderCode(), toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getInvoiceNumber(),
                dto.getTotalPrice(), dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getReceiverPhoneNumber(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage()
        );
    }

}
