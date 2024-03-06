package springboot.shoppingmall.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExchangeEndResultDto {
    private OrderItemDto exchangeOrderItem;
    private OrderDto newOrder;
}
