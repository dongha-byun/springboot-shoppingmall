package springboot.shoppingmall.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseUserOrderAmount {
    private Long userId;
    private int orderCount;
    private int amount;
}
