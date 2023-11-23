package springboot.shoppingmall.partners.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.client.userservice.request.RequestPartnerAuth;
import springboot.shoppingmall.client.userservice.response.ResponsePartnerAuthInfo;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerLoginRepository;

@RequiredArgsConstructor
@Service
public class PartnerLoginService {
    private final PartnerLoginRepository loginRepository;
    private final UserServiceClient userServiceClient;

    public String login(String loginId, String password) {
        Partner partner = loginRepository.findByLoginId(loginId);
        validatePartnerLogin(partner, password);

        ResponsePartnerAuthInfo responsePartnerAuthInfo = userServiceClient.authPartner(
                new RequestPartnerAuth(partner.getId())
        );

        return responsePartnerAuthInfo.getAccessToken();
    }

    private void validatePartnerLogin(Partner partner, String password) {
        if(!partner.isApproved()) {
            throw new IllegalStateException("아직 판매 승인이 처리되지 않아 로그인할 수 없습니다.");
        }

        if(!partner.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
    }
}
