package springboot.shoppingmall.client.payservice;

import org.springframework.scheduling.annotation.Async;

public interface PayServiceClient {

    @Async
    void cancel(Long orderId, int cancelAmount);
}
