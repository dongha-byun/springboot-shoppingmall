package springboot.shoppingmall.order.partners.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryResponse {
    private Long id;
    private String orderCode;
    private String orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private String orderUserName;
    private String orderUserTelNo;
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public static PartnersOrderQueryResponse to(PartnersOrderQueryDto dto) {
        return new PartnersOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getOrderUserName(),
                dto.getOrderUserTelNo(), dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                null
        );
    }
}
