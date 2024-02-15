package springboot.shoppingmall.partners.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.application.request.PartnerRegisterRequestDto;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class PartnerService {
    private final PartnerRepository repository;

    public Long register(PartnerRegisterRequestDto registerRequestDto) {
        if(!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Partner partner = registerRequestDto.toEntity();
        repository.save(partner);
        return partner.getId();
    }
}
