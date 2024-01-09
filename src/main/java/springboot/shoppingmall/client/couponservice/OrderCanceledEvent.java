package springboot.shoppingmall.client.couponservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderCanceledEvent {
    private Long userCouponId;
}
