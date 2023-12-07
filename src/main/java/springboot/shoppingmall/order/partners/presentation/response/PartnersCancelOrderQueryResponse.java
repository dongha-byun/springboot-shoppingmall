package springboot.shoppingmall.order.partners.presentation.response;

import static springboot.shoppingmall.utils.DateUtils.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;

@NoArgsConstructor
@Getter
@Setter
public class PartnersCancelOrderQueryResponse extends PartnersOrderQueryResponse{
    private String resolutionType;
    private String resolutionDate;
    private String resolutionReason;

    public PartnersCancelOrderQueryResponse(Long orderItemId, Long orderId, String orderCode, String orderDate, String productCode,
                                            String productName, int quantity, String invoiceNumber, int totalPrice,
                                            String userName, String userTelNo, String orderStatusName,
                                            String resolutionType, String resolutionDate, String resolutionReason) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity,
                invoiceNumber, totalPrice, userName, userTelNo, orderStatusName);
        this.resolutionType = resolutionType;
        this.resolutionDate = resolutionDate;
        this.resolutionReason = resolutionReason;
    }

    public static PartnersCancelOrderQueryResponse of(PartnersCancelOrderQueryDto dto) {
        return new PartnersCancelOrderQueryResponse(
                dto.getOrderItemId(), dto.getOrderId(), dto.getOrderCode(), toStringOfLocalDateTIme(dto.getOrderDate()),
                dto.getProductCode(), dto.getProductName(), dto.getQuantity(), dto.getInvoiceNumber(),
                dto.getTotalPrice(), dto.getUserName(), dto.getUserTelNo(), dto.getOrderStatus().getStatusName(),
                dto.getResolutionType().name(), toStringOfLocalDateTIme(dto.getResolutionDate()), dto.getResolutionReason()
        );
    }
}
