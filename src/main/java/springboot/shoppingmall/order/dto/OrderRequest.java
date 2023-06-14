package springboot.shoppingmall.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String tid;
    private String payType;
    private List<OrderItemRequest> items;
    private int deliveryFee;
    private DeliveryInfoRequest deliveryInfo;
}
