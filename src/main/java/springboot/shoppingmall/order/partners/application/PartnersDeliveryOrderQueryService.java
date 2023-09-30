package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;

@RequiredArgsConstructor
public class PartnersDeliveryOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    public List<PartnersDeliveryOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        return queryRepository.findPartnersDeliveryOrders(partnerId, startDate, endDate);
    }
}
