package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;

@RequiredArgsConstructor
public class PartnersEndOrderQueryService implements PartnersOrderQueryService{

    private final PartnersOrderQueryRepository queryRepository;
    private final OrderUserInterfaceService orderUserInterfaceService;

    public List<PartnersEndOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                             LocalDateTime endDate) {
        List<PartnersEndOrderQueryDto> orders = queryRepository.findPartnersEndOrders(
                partnerId, startDate, endDate);
        List<Long> userIds = extractUserIds(orders);
        Map<Long, ResponseOrderUserInformation> map = getUserInformation(orderUserInterfaceService, userIds);

        updateOrdersInUserInformation(orders, map);
        return orders;
    }

    private List<Long> extractUserIds(List<PartnersEndOrderQueryDto> partnersReadyOrders) {
        return partnersReadyOrders.stream()
                .map(PartnersEndOrderQueryDto::getUserId)
                .collect(Collectors.toList());
    }

    private void updateOrdersInUserInformation(List<PartnersEndOrderQueryDto> orders,
                                               Map<Long, ResponseOrderUserInformation> map) {
        orders.forEach(orderQueryDto -> {
            Long userId = orderQueryDto.getUserId();
            ResponseOrderUserInformation userInformation = map.get(userId);
            if (userInformation != null) {
                orderQueryDto.setUserName(userInformation.getUserName());
                orderQueryDto.setUserTelNo(userInformation.getUserTelNo());
            }
        });
    }
}
