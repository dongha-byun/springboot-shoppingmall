package springboot.shoppingmall.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.application.dto.DeliveryInfoCreateDto;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;
import springboot.shoppingmall.order.application.dto.OrderItemCreateDto;
import springboot.shoppingmall.payment.domain.PayType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String tid;
    private String orderCode;
    private String payType;
    private List<OrderItemRequest> items;
    private int deliveryFee;
    private DeliveryInfoRequest deliveryInfo;

    public OrderCreateDto toDto() {
        List<OrderItemCreateDto> orderItems = items.stream()
                .map(OrderItemRequest::toDto)
                .collect(Collectors.toList());
        DeliveryInfoCreateDto deliveryInfoCreateDto = deliveryInfo.toDto();

        return new OrderCreateDto(
                tid, orderCode, PayType.valueOf(payType), orderItems,
                deliveryFee, deliveryInfoCreateDto
        );
    }
}
