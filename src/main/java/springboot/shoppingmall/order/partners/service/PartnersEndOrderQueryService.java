package springboot.shoppingmall.order.partners.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.controller.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

@RequiredArgsConstructor
public class PartnersEndOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        List<PartnersEndOrderQueryDto> results =
                queryRepository.findPartnersEndOrders(partnerId, startDate, endDate);

        List<Long> orderIds = results.stream()
                .map(PartnersOrderQueryDto::getId)
                .collect(toList());
        List<PartnersOrderItemQueryDto> orderItems = queryRepository.findOrderItemDtoByOrderIds(orderIds);
        Map<Long, List<PartnersOrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(PartnersOrderItemQueryDto::getOrderId));

        return results.stream()
                .map(dto -> PartnersEndOrderQueryResponse.to(
                                dto, orderItemMap.get(dto.getId())
                ))
                .collect(Collectors.toList());
    }
}
