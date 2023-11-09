package springboot.shoppingmall.partners.application;

import static springboot.shoppingmall.partners.dto.PartnerDto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.partners.dto.PartnerDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;

    @Transactional
    public PartnerDto createPartner(PartnerDto dto) {
        Partner partner = partnerRepository.save(to(dto));
        return of(partner);
    }
}
