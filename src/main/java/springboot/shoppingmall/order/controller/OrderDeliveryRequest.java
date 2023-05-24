package springboot.shoppingmall.order.controller;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDeliveryRequest {
    private LocalDateTime deliveryStartDate;
}
