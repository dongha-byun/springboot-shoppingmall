package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.application.dto.ExchangeEndResultDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExchangeEndResultResponse {
    private OrderItemResponse exchangeOrderItem;
    private OrderResponse newOrder;

    public static ExchangeEndResultResponse of(ExchangeEndResultDto dto) {
        return new ExchangeEndResultResponse(
                OrderItemResponse.of(dto.getExchangeOrderItem()),
                OrderResponse.of(dto.getNewOrder())
        );
    }
}
