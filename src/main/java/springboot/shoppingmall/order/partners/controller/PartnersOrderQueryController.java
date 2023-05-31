package springboot.shoppingmall.order.partners.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryService;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class PartnersOrderQueryController {

    private final Map<PartnersOrderQueryType, PartnersOrderQueryService> partnersOrderQueryServiceMap;

    @GetMapping("/partners/orders")
    public ResponseEntity<PagingDataResponse<List<PartnersOrderQueryResponse>>> findPartnersOrders(
            @LoginPartner AuthorizedPartner partner,
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDateParam,
            @RequestParam("endDate") String endDateParam
    ) {
        PartnersOrderQueryService partnersOrderQueryService =
                partnersOrderQueryServiceMap.get(PartnersOrderQueryType.valueOf(type));

        LocalDateTime startDate = DateUtils.toStartDate(startDateParam);
        LocalDateTime endDate = DateUtils.toEndDate(endDateParam);
        List<PartnersOrderQueryResponse> partnersOrders = partnersOrderQueryService.findPartnersOrders(
                partner.getId(), startDate, endDate
        );

        return ResponseEntity.ok().body(new PagingDataResponse<>(partnersOrders));
    }
}
