package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.presentation.response.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;

@RequiredArgsConstructor
public class PartnersEndOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        List<PartnersEndOrderQueryDto> results =
                queryRepository.findPartnersEndOrders(partnerId, startDate, endDate);

        return results.stream()
                .map(PartnersEndOrderQueryResponse::to)
                .collect(Collectors.toList());
    }
}
