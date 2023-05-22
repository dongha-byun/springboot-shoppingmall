package springboot.shoppingmall.order.partners.service;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.controller.PartnersDeliveryOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;

@RequiredArgsConstructor
public class PartnersDeliveryOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        List<PartnersDeliveryOrderQueryDto> result =
                queryRepository.findPartnersDeliveryOrders(partnerId, startDate, endDate);

        return result.stream()
                .map(PartnersDeliveryOrderQueryResponse::to)
                .collect(toList());
    }
}
