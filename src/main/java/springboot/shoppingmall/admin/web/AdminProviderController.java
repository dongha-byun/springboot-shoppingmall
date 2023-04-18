package springboot.shoppingmall.admin.web;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.admin.service.AdminProviderService;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.web.ProviderResponse;

@RequiredArgsConstructor
@RestController
public class AdminProviderController {

    private final AdminProviderService adminProviderService;

    @GetMapping("/admin/partners")
    public ResponseEntity<List<ProviderResponse>> findAllPartners() {
        List<ProviderResponse> partners = adminProviderService.findAllPartners()
                .stream()
                .map(ProviderResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(partners);
    }

    @PutMapping("/admin/provider/{providerId}/approve")
    public ResponseEntity<APIResult<ProviderApproveResponse>> approveProvider(@PathVariable("providerId") Long providerId) {
        ProviderDto providerDto = adminProviderService.approveProvider(providerId);
        ProviderApproveResponse response = new ProviderApproveResponse(providerDto.getId(), providerDto.isApproved());
        return ResponseEntity.ok(new APIResult<>("판매자격 승인이 완료되었습니다.", response));
    }

    @PutMapping("/admin/provider/{providerId}/stop")
    public ResponseEntity<APIResult<ProviderApproveResponse>> stopProvider(@PathVariable("providerId") Long providerId) {
        ProviderDto providerDto = adminProviderService.stopProvider(providerId);
        ProviderApproveResponse response = new ProviderApproveResponse(providerDto.getId(), providerDto.isApproved());
        return ResponseEntity.ok(new APIResult<>("해당 판매자의 판매 자격이 중지되었습니다.", response));
    }

}
