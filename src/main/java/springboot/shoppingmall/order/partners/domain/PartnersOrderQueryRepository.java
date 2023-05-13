package springboot.shoppingmall.order.partners.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;

public interface PartnersOrderQueryRepository {
    List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnerId, PartnersOrderQueryType status,
                                                        LocalDateTime startDate, LocalDateTime endDate);

    List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnerId,
                                                                  LocalDateTime startDate, LocalDateTime endDate);
}
