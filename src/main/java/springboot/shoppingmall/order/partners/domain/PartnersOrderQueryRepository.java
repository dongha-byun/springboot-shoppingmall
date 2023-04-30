package springboot.shoppingmall.order.partners.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

public interface PartnersOrderQueryRepository {
    List<PartnersOrderQueryDto> findPartnersOrders(Long partnerId, OrderStatus status, LocalDateTime startDate,
                                                   LocalDateTime endDate);
}
