package springboot.shoppingmall.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.service.dto.DeliveryInfoCreateDto;
import springboot.shoppingmall.order.service.dto.OrderCreateDto;
import springboot.shoppingmall.order.service.dto.OrderItemCreateDto;
import springboot.shoppingmall.payment.domain.PayType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String tid;
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
                tid, PayType.valueOf(payType), orderItems,
                deliveryFee, deliveryInfoCreateDto
        );
    }
}
