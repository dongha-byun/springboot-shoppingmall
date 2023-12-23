package springboot.shoppingmall.partners.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.client.userservice.request.RequestPartnerAuth;
import springboot.shoppingmall.client.userservice.response.ResponsePartnerAuthInfo;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerLoginRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerLoginService {
    private final PartnerLoginRepository loginRepository;
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public String login(String loginId, String password) {
        Partner partner = loginRepository.findByLoginId(loginId);
        validatePartnerLogin(partner, password);

        log.info("before call user feign client");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("auth-circuit-breaker");
        ResponsePartnerAuthInfo responsePartnerAuthInfo =
                circuitBreaker.run(() -> userServiceClient.authPartner(new RequestPartnerAuth(partner.getId())),
                        throwable -> new ResponsePartnerAuthInfo("인증 서버 오류"));
        log.info("after call user feign client");

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
