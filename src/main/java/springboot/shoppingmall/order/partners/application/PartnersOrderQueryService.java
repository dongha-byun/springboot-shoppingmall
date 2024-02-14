package springboot.shoppingmall.order.partners.application;

import java.util.List;
import java.util.Map;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.partners.factory.ResponseOrderUserInformationMapFactory;

public interface PartnersOrderQueryService {

    default Map<Long, ResponseOrderUserInformation> getUserInformation(
            OrderUserInterfaceService orderUserInterfaceService, List<Long> userIds) {

        List<ResponseOrderUserInformation> result = orderUserInterfaceService.getUsersOfOrders(userIds);
        return ResponseOrderUserInformationMapFactory.create(result);
    }
}
