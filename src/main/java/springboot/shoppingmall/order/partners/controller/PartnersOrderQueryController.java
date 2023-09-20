package springboot.shoppingmall.order.partners.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.service.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersReadyOrderQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;
import springboot.shoppingmall.utils.DateUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PartnersOrderQueryController {

    private final Map<PartnersOrderQueryType, PartnersOrderQueryService> partnersOrderQueryServiceMap;
    private final PartnersReadyOrderQueryService partnersReadyOrderQueryService;
    private final PartnersDeliveryOrderQueryService partnersDeliveryOrderQueryService;
    private final PartnersEndOrderQueryService partnersEndOrderQueryService;
    private final PartnersCancelOrderQueryService partnersCancelOrderQueryService;

    @GetMapping("/partners/orders")
    public ResponseEntity<List<PartnersOrderQueryResponse>> findPartnersOrders(
            @LoginPartner AuthorizedPartner partner,
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDateParam,
            @RequestParam("endDate") String endDateParam
    ) {
        PartnersOrderQueryType partnersOrderQueryType = PartnersOrderQueryType.valueOf(type);
        LocalDateTime startDate = DateUtils.toStartDate(startDateParam);
        LocalDateTime endDate = DateUtils.toEndDate(endDateParam);

        List<PartnersOrderQueryResponse> partnersOrders = new ArrayList<>();
        if(PartnersOrderQueryType.READY == partnersOrderQueryType) {
            partnersOrders = partnersReadyOrderQueryService.findPartnersOrders(
                    partner.getId(), startDate, endDate
            );
        }else if(PartnersOrderQueryType.DELIVERY == partnersOrderQueryType) {
            partnersOrders = partnersDeliveryOrderQueryService.findPartnersOrders(
                    partner.getId(), startDate, endDate
            );
        }else if(PartnersOrderQueryType.END == partnersOrderQueryType) {
            partnersOrders = partnersEndOrderQueryService.findPartnersOrders(
                    partner.getId(), startDate, endDate
            );
        }else if(PartnersOrderQueryType.CANCEL == partnersOrderQueryType) {
            partnersOrders = partnersCancelOrderQueryService.findPartnersOrders(
                    partner.getId(), startDate, endDate
            );
        }

        log.info("result = {}", partnersOrders);
        return ResponseEntity.ok().body(partnersOrders);
    }
}
