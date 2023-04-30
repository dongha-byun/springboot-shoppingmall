package springboot.shoppingmall.order.configuration;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryJPARepository;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
@Configuration
public class OrderConfiguration {

    private final EntityManager entityManager;

    @Bean
    public PartnersOrderQueryRepository partnersOrderQueryRepository() {
        return new PartnersOrderQueryJPARepository(entityManager);
    }
}
