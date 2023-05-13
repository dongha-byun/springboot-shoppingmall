package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;

public interface PartnersOrderQueryServiceInterface {

    List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate, LocalDateTime endDate);
}
