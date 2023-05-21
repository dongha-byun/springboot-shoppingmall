package springboot.shoppingmall.order.partners.service;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.order.partners.controller.PartnersDeliveryOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

@RequiredArgsConstructor
public class PartnersDeliveryOrderQueryService implements PartnersOrderQueryService {

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        List<PartnersDeliveryOrderQueryDto> result =
                queryRepository.findPartnersDeliveryOrders(partnerId, startDate, endDate);

        List<Long> orderIds = result.stream()
                .map(PartnersOrderQueryDto::getId)
                .collect(toList());
        List<PartnersOrderItemQueryDto> orderItems = queryRepository.findOrderItemDtoByOrderIds(orderIds);
        Map<Long, List<PartnersOrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(PartnersOrderItemQueryDto::getOrderId));

        return result.stream()
                .map(dto -> PartnersDeliveryOrderQueryResponse.to(
                        dto, orderItemMap.get(dto.getId())
                ))
                .collect(toList());
    }
}
