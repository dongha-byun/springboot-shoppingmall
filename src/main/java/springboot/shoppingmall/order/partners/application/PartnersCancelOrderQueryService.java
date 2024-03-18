package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
public class PartnersCancelOrderQueryService implements PartnersOrderQueryService<PartnersCancelOrderQueryDto>{

    private final PartnersOrderQueryRepository queryRepository;
    private final OrderUserInterfaceService orderUserInterfaceService;

    public List<PartnersCancelOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        List<PartnersCancelOrderQueryDto> orders =
                queryRepository.findPartnersCancelOrders(partnerId, startDate, endDate);

        return fillUserInfoOfOrders(orderUserInterfaceService, orders);
    }
}
