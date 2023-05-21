package springboot.shoppingmall.order.partners.controller;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;
import springboot.shoppingmall.utils.DateUtils;

@NoArgsConstructor
@Getter
public class PartnersDeliveryOrderQueryResponse extends PartnersOrderQueryResponse{
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public PartnersDeliveryOrderQueryResponse(Long id, String orderCode, String orderDate,
                                              List<PartnersOrderItemQueryResponse> items, int totalPrice,
                                              String userName, String userTelNo, String orderStatusName,
                                              String receiverName, String address, String detailAddress,
                                              String requestMessage) {
        super(id, orderCode, orderDate, items, totalPrice, userName, userTelNo, orderStatusName);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }

    public static PartnersDeliveryOrderQueryResponse to(
            PartnersDeliveryOrderQueryDto dto,
            List<PartnersOrderItemQueryDto> orderItemQueryDtos
    ) {
        List<PartnersOrderItemQueryResponse> itemQueryResponses = orderItemDtoToResponse(orderItemQueryDtos);
        return new PartnersDeliveryOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), DateUtils.toStringOfLocalDateTIme(dto.getOrderDate()),
                itemQueryResponses, dto.getTotalPrice(),
                dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress(),
                dto.getRequestMessage()
        );
    }
}
