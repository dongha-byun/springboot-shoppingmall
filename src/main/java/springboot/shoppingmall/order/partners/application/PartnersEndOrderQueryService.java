package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
public class PartnersEndOrderQueryService implements PartnersOrderQueryService<PartnersEndOrderQueryDto>{

    private final PartnersOrderQueryRepository queryRepository;
    private final OrderUserInterfaceService orderUserInterfaceService;

    public List<PartnersEndOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                             LocalDateTime endDate) {
        List<PartnersEndOrderQueryDto> orders =
                queryRepository.findPartnersEndOrders(partnerId, startDate, endDate);

        return fillUserInfoOfOrders(orderUserInterfaceService, orders);
    }

}
