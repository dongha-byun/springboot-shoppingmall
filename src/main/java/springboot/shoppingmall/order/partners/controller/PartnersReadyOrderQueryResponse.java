package springboot.shoppingmall.order.partners.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersReadyOrderQueryResponse {
    private Long id;
    private String orderCode;
    private String orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private int totalPrice;
    private String userName;
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String orderStatusName;
    private String invoiceNumber;

    public static PartnersReadyOrderQueryResponse to(PartnersReadyOrderQueryDto dto) {
        return new PartnersReadyOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getTotalPrice(),
                dto.getUserName(), dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage(), dto.getOrderStatus().getStatusName(), dto.getInvoiceNumber()
        );
    }
}
