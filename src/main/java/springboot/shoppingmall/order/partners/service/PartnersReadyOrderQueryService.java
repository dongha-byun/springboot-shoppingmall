package springboot.shoppingmall.order.partners.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;

@RequiredArgsConstructor
@Slf4j
public class PartnersReadyOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        List<PartnersReadyOrderQueryDto> result =
                queryRepository.findPartnersReadyOrders(partnerId, startDate, endDate);

        List<Long> orderIds = result.stream()
                .map(PartnersOrderQueryDto::getId)
                .collect(toList());
        List<PartnersOrderItemQueryDto> orderItems = queryRepository.findOrderItemDtoByOrderIds(orderIds);
        Map<Long, List<PartnersOrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(PartnersOrderItemQueryDto::getOrderId));

        return result.stream()
                .map(dto -> PartnersReadyOrderQueryResponse.to(
                        dto, orderItemMap.get(dto.getId())
                ))
                .collect(Collectors.toList());
    }
}
