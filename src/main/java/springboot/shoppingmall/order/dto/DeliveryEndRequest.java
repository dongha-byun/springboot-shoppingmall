package springboot.shoppingmall.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryEndRequest {
    private LocalDateTime deliveryDate;
    private String deliveryPlace;
}
