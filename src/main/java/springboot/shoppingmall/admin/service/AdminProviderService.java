package springboot.shoppingmall.admin.service;

import static springboot.shoppingmall.providers.dto.ProviderDto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.providers.dto.ProviderDto;

@RequiredArgsConstructor
@Service
public class AdminProviderService {
    private final ProviderRepository providerRepository;

    @Transactional
    public ProviderDto approveProvider(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(
                        () -> new IllegalArgumentException("판매자 조회 오류")
                );
        provider.approve();
        return of(provider);
    }
}
