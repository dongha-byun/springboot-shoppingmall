package springboot.shoppingmall.order.partners.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.service.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryService;
import springboot.shoppingmall.order.partners.service.PartnersReadyOrderQueryService;

@Configuration
public class PartnersOrderQueryConfiguration {

    private final PartnersOrderQueryRepository repository;

    public PartnersOrderQueryConfiguration(PartnersOrderQueryRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Map<PartnersOrderQueryType, PartnersOrderQueryService> partnersOrderQueryServiceMap() {
        Map<PartnersOrderQueryType, PartnersOrderQueryService> map = new HashMap<>();
        map.put(PartnersOrderQueryType.READY, partnersReadyOrderQueryService());
        map.put(PartnersOrderQueryType.DELIVERY, partnersDeliveryOrderQueryService());
        map.put(PartnersOrderQueryType.END, partnersEndOrderQueryService());
        map.put(PartnersOrderQueryType.CANCEL, partnersCancelOrderQueryService());

        return map;
    }

    @Bean
    public PartnersReadyOrderQueryService partnersReadyOrderQueryService() {
        return new PartnersReadyOrderQueryService(repository);
    }

    @Bean
    public PartnersDeliveryOrderQueryService partnersDeliveryOrderQueryService() {
        return new PartnersDeliveryOrderQueryService(repository);
    }

    @Bean
    public PartnersEndOrderQueryService partnersEndOrderQueryService() {
        return new PartnersEndOrderQueryService(repository);
    }

    @Bean
    public PartnersCancelOrderQueryService partnersCancelOrderQueryService() {
        return new PartnersCancelOrderQueryService(repository);
    }
}
