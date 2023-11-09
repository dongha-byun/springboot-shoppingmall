package springboot.shoppingmall.partners.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerLoginRepository;
import springboot.shoppingmall.partners.dto.PartnerDto;

@RequiredArgsConstructor
@Service
public class PartnerLoginService {
    private final PartnerLoginRepository loginRepository;

    public PartnerDto login(String loginId, String password) {
        Partner partner = loginRepository.findByLoginId(loginId);

        if(!partner.isApproved()) {
            throw new IllegalStateException("아직 판매 승인이 처리되지 않아 로그인할 수 없습니다.");
        }

        if(!partner.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        return PartnerDto.of(partner);
    }
}
