package springboot.shoppingmall.pay.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.dto.OrderRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PayApproveRequest<T>{
    private String type;
    private T data;
    private OrderRequest orderData;
}
