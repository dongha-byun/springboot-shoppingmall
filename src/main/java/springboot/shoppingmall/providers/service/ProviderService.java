package springboot.shoppingmall.providers.service;

import static springboot.shoppingmall.providers.dto.ProviderDto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.providers.dto.ProviderDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    @Transactional
    public ProviderDto createProvider(ProviderDto dto) {
        Provider provider = providerRepository.save(to(dto));
        return of(provider);
    }
}
