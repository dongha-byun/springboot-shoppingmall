package springboot.shoppingmall.order.partners.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryDto {
    private Long id;
    private String orderCode;
    private LocalDateTime orderDate;
    private int totalPrice;
    private String userName;
    private String userTelNo;
    private OrderStatus orderStatus;
}
