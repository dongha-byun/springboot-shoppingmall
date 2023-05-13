package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
@Slf4j
public class PartnersCancelOrderQueryServiceImpl implements PartnersOrderQueryServiceInterface{

    private final PartnersOrderQueryRepository queryRepository;

    @Override
    public List<PartnersOrderQueryResponse> findPartnersOrders(Long partnerId, LocalDateTime startDate,
                                                               LocalDateTime endDate) {
        log.info("Cancel Type Service");
        return null;
    }
}
