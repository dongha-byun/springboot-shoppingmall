package springboot.shoppingmall.order.partners.controller;

import static springboot.shoppingmall.utils.DateUtils.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.partners.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;

@NoArgsConstructor
@Getter
@Setter
public class PartnersCancelOrderQueryResponse extends PartnersOrderQueryResponse{
    private String cancelDate;
    private String cancelReason;
    private String refundDate;
    private String refundReason;
    private String exchangeDate;
    private String exchangeReason;

    public PartnersCancelOrderQueryResponse(Long id, String orderCode, String orderDate,
                                            List<PartnersOrderItemQueryResponse> items, int totalPrice,
                                            String userName, String userTelNo, String orderStatusName,
                                            String cancelDate, String cancelReason,
                                            String refundDate, String refundReason,
                                            String exchangeDate, String exchangeReason) {
        super(id, orderCode, orderDate, items, totalPrice, userName, userTelNo, orderStatusName);
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
        this.exchangeDate = exchangeDate;
        this.exchangeReason = exchangeReason;
    }

    public static PartnersCancelOrderQueryResponse to(
            PartnersCancelOrderQueryDto dto,
            List<PartnersOrderItemQueryDto> itemQueryDtos
    ) {
        List<PartnersOrderItemQueryResponse> itemQueryResponses = orderItemDtoToResponse(itemQueryDtos);
        return new PartnersCancelOrderQueryResponse(
                dto.getId(), dto.getOrderCode(), toStringOfLocalDateTIme(dto.getOrderDate()),
                itemQueryResponses, dto.getTotalPrice(),
                dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                toStringOfLocalDateTIme(dto.getCancelDate()), dto.getCancelReason(),
                toStringOfLocalDateTIme(dto.getRefundDate()), dto.getRefundReason(),
                toStringOfLocalDateTIme(dto.getExchangeDate()), dto.getExchangeReason()
        );
    }
}
