package springboot.shoppingmall.order.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemResolutionResponse {
    private Long orderItemId;
    private String message;
}
