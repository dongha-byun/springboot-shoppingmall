package springboot.shoppingmall.order.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDto {
    private Long id;
    private String orderCode;
    private Long userId;
    private int totalPrice;
    private int realPayPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
    private OrderDeliveryInfoDto deliveryInfo;

    public static OrderDto of(Order entity) {
        List<OrderItemDto> items = entity.getItems()
                .stream()
                .map(OrderItemDto::of)
                .collect(Collectors.toList());
        OrderDeliveryInfoDto orderDeliveryInfoDto =
                OrderDeliveryInfoDto.of(entity.getOrderDeliveryInfo());
        return new OrderDto(
                entity.getId(), entity.getOrderCode(), entity.getUserId(),
                entity.getTotalPrice(), entity.getRealPayPrice(),
                entity.getOrderDate(), items, orderDeliveryInfoDto
        );
    }
}
