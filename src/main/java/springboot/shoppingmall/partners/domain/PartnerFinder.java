package springboot.shoppingmall.partners.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PartnerFinder {

    private final PartnerRepository repository;

    public Partner findById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("판매자 조회 오류")
                );
    }

    public List<Partner> findAll() {
        return repository.findAll();
    }
}
