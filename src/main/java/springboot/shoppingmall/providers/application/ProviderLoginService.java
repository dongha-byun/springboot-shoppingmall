package springboot.shoppingmall.providers.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderLoginRepository;
import springboot.shoppingmall.providers.dto.ProviderDto;

@RequiredArgsConstructor
@Service
public class ProviderLoginService {
    private final ProviderLoginRepository loginRepository;

    public ProviderDto login(String loginId, String password) {
        Provider provider = loginRepository.findByLoginId(loginId);

        if(!provider.isApproved()) {
            throw new IllegalStateException("아직 판매 승인이 처리되지 않아 로그인할 수 없습니다.");
        }

        if(!provider.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        return ProviderDto.of(provider);
    }
}
