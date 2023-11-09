package springboot.shoppingmall.admin.web;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.admin.service.AdminPartnerService;
import springboot.shoppingmall.partners.dto.PartnerDto;
import springboot.shoppingmall.partners.presentation.response.PartnerResponse;

@RequiredArgsConstructor
@RestController
public class AdminPartnerController {

    private final AdminPartnerService adminPartnerService;

    @GetMapping("/admin/partners")
    public ResponseEntity<List<PartnerResponse>> findAllPartners() {
        List<PartnerResponse> partners = adminPartnerService.findAllPartners()
                .stream()
                .map(PartnerResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(partners);
    }

    @PutMapping("/admin/partners/{partnerId}/approve")
    public ResponseEntity<APIResult<PartnerApproveResponse>> approvePartner(@PathVariable("partnerId") Long partnerId) {
        PartnerDto partnerDto = adminPartnerService.approvePartner(partnerId);
        PartnerApproveResponse response = new PartnerApproveResponse(partnerDto.getId(), partnerDto.isApproved());
        return ResponseEntity.ok(new APIResult<>("판매자격 승인이 완료되었습니다.", response));
    }

    @PutMapping("/admin/partners/{partnerId}/stop")
    public ResponseEntity<APIResult<PartnerApproveResponse>> stopPartner(@PathVariable("partnerId") Long partnerId) {
        PartnerDto partnerDto = adminPartnerService.stopPartner(partnerId);
        PartnerApproveResponse response = new PartnerApproveResponse(partnerDto.getId(), partnerDto.isApproved());
        return ResponseEntity.ok(new APIResult<>("해당 판매자의 판매 자격이 중지되었습니다.", response));
    }

}
