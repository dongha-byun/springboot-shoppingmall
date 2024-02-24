package springboot.shoppingmall.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerFinder;
import springboot.shoppingmall.partners.dto.PartnerDto;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminPartnerService {
    private final PartnerFinder partnerFinder;

    @Transactional(readOnly = true)
    public List<PartnerDto> findAllPartners() {
        return partnerFinder.findAll()
                .stream()
                .map(PartnerDto::of)
                .collect(Collectors.toList());
    }

    public PartnerDto approvePartner(Long partnerId) {
        Partner partner = partnerFinder.findById(partnerId);
        partner.approve();
        return PartnerDto.of(partner);
    }

    public PartnerDto stopPartner(Long partnerId) {
        Partner partner = partnerFinder.findById(partnerId);
        partner.stop();
        return PartnerDto.of(partner);
    }
}
