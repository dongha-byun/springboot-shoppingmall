package springboot.shoppingmall.order.application;

import java.util.List;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;

public interface OrderUserInterfaceService {

    void increaseOrderAmounts(Long userId, int price);

    List<ResponseOrderUserInformation> getUsersOfOrders(List<Long> userIds);

    int getDiscountRate(Long userId);
}
