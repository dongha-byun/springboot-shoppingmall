package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
public class PartnersReadyOrderQueryService implements PartnersOrderQueryService<PartnersReadyOrderQueryDto>{

    private final PartnersOrderQueryRepository queryRepository;
    private final OrderUserInterfaceService orderUserInterfaceService;

    public List<PartnersReadyOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        List<PartnersReadyOrderQueryDto> orders =
                queryRepository.findPartnersReadyOrders(partnerId, startDate, endDate);

        return fillUserInfoOfOrders(orderUserInterfaceService, orders);
    }
}
