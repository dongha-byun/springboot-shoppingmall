package springboot.shoppingmall.product.query.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaQueryResponse;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;
import springboot.shoppingmall.product.query.application.PartnerProductQnaQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class PartnersProductQnaQueryController {
    private final PartnerProductQnaQueryService partnerProductQnaQueryService;

    @GetMapping("/partners/product/qnas")
    public ResponseEntity<PagingDataResponse<List<PartnersProductQnaQueryResponse>>> findPartnersProductQnas(
            @LoginPartner AuthorizedPartner partner,
            @RequestParam("isAnswered") String isAnswered
    ) {
        ProductQnaAnswerCompleteType completeType = ProductQnaAnswerCompleteType.valueOf(isAnswered);
        List<PartnersProductQnaDto> partnersProductQnaDtos = partnerProductQnaQueryService.findPartnersProductQna(
                partner.getId(), completeType);

        List<PartnersProductQnaQueryResponse> responses = partnersProductQnaDtos.stream()
                .map(PartnersProductQnaQueryResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new PagingDataResponse<>(responses));
    }
}
