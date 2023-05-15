package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springboot.shoppingmall.order.partners.controller.PartnersCancelOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersCancelOrderQueryDto;

@RequiredArgsConstructor
@Slf4j
public class PartnersCancelOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        List<PartnersCancelOrderQueryDto> results = queryRepository.findPartnersCancelOrders(partnerId,
                startDate, endDate);
        return results.stream()
                .map(PartnersCancelOrderQueryResponse::to)
                .collect(Collectors.toList());
    }
}
