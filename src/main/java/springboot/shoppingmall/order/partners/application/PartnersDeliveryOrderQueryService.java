package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
public class PartnersDeliveryOrderQueryService implements PartnersOrderQueryService<PartnersDeliveryOrderQueryDto>{

    private final PartnersOrderQueryRepository queryRepository;
    private final OrderUserInterfaceService orderUserInterfaceService;

    public List<PartnersDeliveryOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                                  LocalDateTime endDate) {
        List<PartnersDeliveryOrderQueryDto> orders =
                queryRepository.findPartnersDeliveryOrders(partnerId, startDate, endDate);

        return fillUserInfoOfOrders(orderUserInterfaceService, orders);
    }
}
