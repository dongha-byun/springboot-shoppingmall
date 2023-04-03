package springboot.shoppingmall.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.admin.service.AdminProviderService;
import springboot.shoppingmall.providers.dto.ProviderDto;

@RequiredArgsConstructor
@RestController
public class AdminProviderController {

    private final AdminProviderService adminProviderService;

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
