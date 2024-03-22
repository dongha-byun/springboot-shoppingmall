package springboot.shoppingmall.order.infra.openfeign;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;

@RequiredArgsConstructor
@Primary
@Component
public class FeignClientOrderUserInterfaceService implements OrderUserInterfaceService {
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public void increaseOrderAmounts(Long userId, int price) {
        userServiceClient.increaseOrderAmounts(userId, price);
    }

    @Override
    public List<ResponseOrderUserInformation> getUsersOfOrders(List<Long> userIds) {
        return userServiceClient.getUsersOfOrders(userIds);
    }

    @Override
    public int getDiscountRate(Long userId) {
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-discount-rate");
//        int discountRate = circuitBreaker.run(
//                () -> userServiceClient.getDiscountRate(userId),
//                throwable -> 0
//        );
        return userServiceClient.getDiscountRate(userId);
    }


}
