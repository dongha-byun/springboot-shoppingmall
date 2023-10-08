package springboot.shoppingmall.order.partners.factory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;

public class ResponseOrderUserInformationMapFactory {

    public static Map<Long, ResponseOrderUserInformation> create(List<ResponseOrderUserInformation> result) {
        return result.stream()
                .collect(Collectors.toMap(
                        ResponseOrderUserInformation::getUserId,
                        responseOrderUserInformation -> responseOrderUserInformation
                ));
    }
}
