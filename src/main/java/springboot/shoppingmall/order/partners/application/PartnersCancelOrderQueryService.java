package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;

@RequiredArgsConstructor
public class PartnersCancelOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    public List<PartnersCancelOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        return queryRepository.findPartnersCancelOrders(partnerId, startDate, endDate);
    }
}
