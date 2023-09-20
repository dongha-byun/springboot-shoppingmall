package springboot.shoppingmall.order.partners.application;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.partners.presentation.response.PartnersOrderQueryResponse;

public interface PartnersOrderQueryService {

    List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate, LocalDateTime endDate);
}
