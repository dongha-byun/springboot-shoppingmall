package springboot.shoppingmall.order.partners.presentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.order.partners.presentation.response.PartnersCancelOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersDeliveryOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;
import springboot.shoppingmall.order.partners.presentation.response.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class PartnersOrderQueryController {

    //private final Map<PartnersOrderQueryType, PartnersOrderQueryService> partnersOrderQueryServiceMap;
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
        Long partnerId = partner.getId();

        List<PartnersOrderQueryResponse> partnersOrders = new ArrayList<>();
        if(PartnersOrderQueryType.READY == partnersOrderQueryType) {
            List<PartnersReadyOrderQueryDto> orders =
                    partnersReadyOrderQueryService.findPartnersOrders(partnerId, startDate, endDate);
            partnersOrders = orders.stream()
                    .map(PartnersReadyOrderQueryResponse::of)
                    .collect(Collectors.toList());
        }else if(PartnersOrderQueryType.DELIVERY == partnersOrderQueryType) {
            List<PartnersDeliveryOrderQueryDto> orders =
                    partnersDeliveryOrderQueryService.findPartnersOrders(partnerId, startDate, endDate);
            partnersOrders = orders.stream()
                    .map(PartnersDeliveryOrderQueryResponse::of)
                    .collect(Collectors.toList());
        }else if(PartnersOrderQueryType.END == partnersOrderQueryType) {
            List<PartnersEndOrderQueryDto> orders =
                    partnersEndOrderQueryService.findPartnersOrders(partnerId, startDate, endDate);
            partnersOrders = orders.stream()
                    .map(PartnersEndOrderQueryResponse::of)
                    .collect(Collectors.toList());
        }else if(PartnersOrderQueryType.CANCEL == partnersOrderQueryType) {
            List<PartnersCancelOrderQueryDto> orders =
                    partnersCancelOrderQueryService.findPartnersOrders(partnerId, startDate, endDate);
            partnersOrders = orders.stream()
                    .map(PartnersCancelOrderQueryResponse::of)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok().body(partnersOrders);
    }
}
