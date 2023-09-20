package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springboot.shoppingmall.order.partners.presentation.response.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;

@RequiredArgsConstructor
@Slf4j
public class PartnersReadyOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        List<PartnersReadyOrderQueryDto> result =
                queryRepository.findPartnersReadyOrders(partnerId, startDate, endDate);

        return result.stream()
                .map(PartnersReadyOrderQueryResponse::to)
                .collect(Collectors.toList());
    }
}
