package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;

@RequiredArgsConstructor
public class PartnersEndOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    public List<PartnersEndOrderQueryDto> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        return queryRepository.findPartnersEndOrders(partnerId, startDate, endDate);
    }
}
