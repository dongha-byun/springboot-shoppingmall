package springboot.shoppingmall.admin.service;

import static springboot.shoppingmall.providers.dto.ProviderDto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderFinder;
import springboot.shoppingmall.providers.dto.ProviderDto;

@RequiredArgsConstructor
@Service
public class AdminProviderService {
    private final ProviderFinder providerFinder;

    @Transactional
    public ProviderDto approveProvider(Long providerId) {
        Provider provider = providerFinder.findById(providerId);
        provider.approve();
        return of(provider);
    }

    @Transactional
    public ProviderDto stopProvider(Long providerId) {
        Provider provider = providerFinder.findById(providerId);
        provider.stop();
        return of(provider);
    }
}
