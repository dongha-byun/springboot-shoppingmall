package springboot.shoppingmall.order.infra.openfeign;

import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void increaseOrderAmounts(Long userId, int price) {
        userServiceClient.increaseOrderAmounts(userId, price);
    }

    @Override
    public List<ResponseOrderUserInformation> getOrderUsers(List<Long> userIds) {
        return userServiceClient.getUsersOfOrders(userIds);
    }
}
