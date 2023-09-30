package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;

@RequiredArgsConstructor
public class PartnersReadyOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    public List<PartnersReadyOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        return queryRepository.findPartnersReadyOrders(partnerId, startDate, endDate);
    }
}
