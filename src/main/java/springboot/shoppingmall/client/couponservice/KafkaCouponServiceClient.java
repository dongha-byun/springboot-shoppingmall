package springboot.shoppingmall.client.couponservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaCouponServiceClient implements CouponServiceClient{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void recoveryCoupon(Long userCouponId) {
        try {
            OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(userCouponId);
            String orderCanceledEventMessage = objectMapper.writeValueAsString(orderCanceledEvent);

            kafkaTemplate.send("order-canceled", orderCanceledEventMessage);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
