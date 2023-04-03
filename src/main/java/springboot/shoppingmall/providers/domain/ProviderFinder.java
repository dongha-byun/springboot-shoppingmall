package springboot.shoppingmall.providers.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProviderFinder {

    private final ProviderRepository repository;

    public Provider findById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("판매자 조회 오류")
                );
    }
}
