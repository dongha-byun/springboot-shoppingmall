package springboot.shoppingmall.order.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.payment.domain.PayType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderCreateDto {
    private String tid;
    private String orderCode;
    private PayType payType;
    private List<OrderItemCreateDto> items;
    private int deliveryFee;
    private DeliveryInfoCreateDto deliveryInfo;
}
