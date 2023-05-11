package springboot.shoppingmall.order.partners.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryService;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class PartnersOrderQueryController {

    private final PartnersOrderQueryService partnersOrderQueryService;

    @GetMapping("/partners/orders/{type}")
    public ResponseEntity<PagingDataResponse<List<PartnersReadyOrderQueryResponse>>> findPartnersReadyOrderAll(
            @LoginPartner AuthorizedPartner partner,
            @PathVariable("type") String type,
            @RequestParam("startDate") String startDateParam,
            @RequestParam("endDate") String endDateParam
    ) {
        LocalDateTime startDate = DateUtils.toStartDate(startDateParam);
        LocalDateTime endDate = DateUtils.toEndDate(endDateParam);
        List<PartnersReadyOrderQueryDto> readyOrders = partnersOrderQueryService.findPartnersReadyOrders(partner.getId(),
                PartnersOrderQueryType.valueOf(type), startDate, endDate);

        List<PartnersReadyOrderQueryResponse> responses = readyOrders.stream()
                .map(PartnersReadyOrderQueryResponse::to)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new PagingDataResponse<>(responses));
    }
}
