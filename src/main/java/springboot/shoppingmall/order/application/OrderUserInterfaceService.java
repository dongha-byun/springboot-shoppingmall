package springboot.shoppingmall.order.application;

import java.util.List;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;

public interface OrderUserInterfaceService {

    int getOrderUserDiscountRate(Long userId);

    void increaseOrderAmounts(Long userId, int price);

    List<ResponseOrderUserInformation> getOrderUsers(List<Long> userIds);
}
