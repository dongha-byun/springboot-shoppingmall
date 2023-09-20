package springboot.shoppingmall.order.partners.domain;

import java.time.LocalDateTime;
import java.util.List;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;

public interface PartnersOrderQueryRepository {

    List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnerId,
                                                             LocalDateTime startDate, LocalDateTime endDate);

    List<PartnersDeliveryOrderQueryDto> findPartnersDeliveryOrders(Long partnerId,
                                                                   LocalDateTime startDate, LocalDateTime endDate);

    List<PartnersEndOrderQueryDto> findPartnersEndOrders(Long partnerId,
                                                         LocalDateTime startDate, LocalDateTime endDate);

    List<PartnersCancelOrderQueryDto> findPartnersCancelOrders(Long partnerId,
                                                               LocalDateTime startDate, LocalDateTime endDate);
}
