package springboot.shoppingmall.order.partners.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Getter
public class PartnersReadyOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public PartnersReadyOrderQueryResponse(Long id, String orderCode, String orderDate,
                                           List<PartnersOrderItemQueryResponse> items,
                                           int totalPrice, String userName, String userTelNo,
                                           String receiverName, String address, String detailAddress,
                                           String requestMessage, String orderStatusName) {
        super(id, orderCode, orderDate, items, totalPrice, userName, userTelNo, orderStatusName);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }

    public static PartnersReadyOrderQueryResponse to(
            PartnersReadyOrderQueryDto dto,
            List<PartnersOrderItemQueryDto> itemQueryDtos
    ) {
        List<PartnersOrderItemQueryResponse> itemQueryResponses = itemQueryDtos.stream()
                .map(PartnersOrderItemQueryResponse::to)
                .collect(Collectors.toList());
        return new PartnersReadyOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                itemQueryResponses, dto.getTotalPrice(),
                dto.getUserName(), dto.getUserTelNo(),
                dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage(), dto.getOrderStatus().getStatusName()
        );
    }

}
