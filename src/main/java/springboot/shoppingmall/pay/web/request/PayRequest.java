package springboot.shoppingmall.pay.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayRequest<T> {
    private String type;
    private T data;
}
