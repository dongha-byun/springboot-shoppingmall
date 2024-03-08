package springboot.shoppingmall.coupon.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.client.couponservice.OrderCanceledEvent;
import springboot.shoppingmall.coupon.application.UserCouponService;

@RequiredArgsConstructor
@Component
public class KafkaCouponServiceListener {
    private final ObjectMapper objectMapper;
    private final UserCouponService userCouponService;

    @KafkaListener(topics = "order-canceled", groupId = "shopping-mall")
    public void recoveryCoupon(String message) {
        try {
            OrderCanceledEvent orderCanceledEvent = objectMapper.readValue(message, OrderCanceledEvent.class);
            userCouponService.recoveryCoupon(orderCanceledEvent.getUserCouponId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
