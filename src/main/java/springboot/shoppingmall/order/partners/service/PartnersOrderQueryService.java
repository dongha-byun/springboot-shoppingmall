package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

public interface PartnersOrderQueryService {

    List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate, LocalDateTime endDate);
}
