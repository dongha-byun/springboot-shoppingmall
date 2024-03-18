package springboot.shoppingmall.order.partners.presentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.common.search.SearchEndDate;
import springboot.shoppingmall.common.search.SearchStartDate;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.order.partners.presentation.response.PartnersCancelOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersDeliveryOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.presentation.response.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class PartnersOrderQueryController {

    private final PartnersReadyOrderQueryService partnersReadyOrderQueryService;
    private final PartnersDeliveryOrderQueryService partnersDeliveryOrderQueryService;
    private final PartnersEndOrderQueryService partnersEndOrderQueryService;
    private final PartnersCancelOrderQueryService partnersCancelOrderQueryService;

    @GetMapping("/partners/orders/READY")
    public ResponseEntity<List<PartnersReadyOrderQueryResponse>> findPartnersReadyOrders(
            @LoginPartner AuthorizedPartner partner,
            @SearchStartDate LocalDateTime searchStartDate,
            @SearchEndDate LocalDateTime searchEndDate
    ) {
        Long partnerId = partner.getId();
        List<PartnersReadyOrderQueryDto> orders =
                partnersReadyOrderQueryService.findPartnersOrders(partnerId, searchStartDate, searchEndDate);

        List<PartnersReadyOrderQueryResponse> result = orders.stream()
                .map(PartnersReadyOrderQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/partners/orders/DELIVERY")
    public ResponseEntity<List<PartnersDeliveryOrderQueryResponse>> findPartnersDeliveryOrders(
            @LoginPartner AuthorizedPartner partner,
            @SearchStartDate LocalDateTime searchStartDate,
            @SearchEndDate LocalDateTime searchEndDate
    ) {
        Long partnerId = partner.getId();
        List<PartnersDeliveryOrderQueryDto> orders =
                partnersDeliveryOrderQueryService.findPartnersOrders(partnerId, searchStartDate, searchEndDate);

        List<PartnersDeliveryOrderQueryResponse> result = orders.stream()
                .map(PartnersDeliveryOrderQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/partners/orders/END")
    public ResponseEntity<List<PartnersEndOrderQueryResponse>> findPartnersEndOrders(
            @LoginPartner AuthorizedPartner partner,
            @SearchStartDate LocalDateTime searchStartDate,
            @SearchEndDate LocalDateTime searchEndDate
    ) {
        Long partnerId = partner.getId();
        List<PartnersEndOrderQueryDto> orders =
                partnersEndOrderQueryService.findPartnersOrders(partnerId, searchStartDate, searchEndDate);

        List<PartnersEndOrderQueryResponse> result = orders.stream()
                .map(PartnersEndOrderQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/partners/orders/CANCEL")
    public ResponseEntity<List<PartnersCancelOrderQueryResponse>> findPartnersOrders(
            @LoginPartner AuthorizedPartner partner,
            @SearchStartDate LocalDateTime searchStartDate,
            @SearchEndDate LocalDateTime searchEndDate
    ) {
        Long partnerId = partner.getId();
        List<PartnersCancelOrderQueryDto> orders =
                partnersCancelOrderQueryService.findPartnersOrders(partnerId, searchStartDate, searchEndDate);

        List<PartnersCancelOrderQueryResponse> result = orders.stream()
                .map(PartnersCancelOrderQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }
}
