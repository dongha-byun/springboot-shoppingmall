package springboot.shoppingmall.order.partners.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.service.PartnersCancelOrderQueryServiceImpl;
import springboot.shoppingmall.order.partners.service.PartnersEndOrderQueryServiceImpl;
import springboot.shoppingmall.order.partners.service.PartnersDeliveryOrderQueryServiceImpl;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryServiceInterface;
import springboot.shoppingmall.order.partners.service.PartnersReadyOrderQueryServiceImpl;

@Configuration
public class PartnersOrderQueryConfiguration {

    private final PartnersOrderQueryRepository repository;

    public PartnersOrderQueryConfiguration(PartnersOrderQueryRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Map<PartnersOrderQueryType, PartnersOrderQueryServiceInterface> partnersOrderQueryServiceMap() {
        Map<PartnersOrderQueryType, PartnersOrderQueryServiceInterface> map = new HashMap<>();
        map.put(PartnersOrderQueryType.READY, partnersReadyOrderQueryService());
        map.put(PartnersOrderQueryType.DELIVERY, partnersDeliveryOrderQueryService());
        map.put(PartnersOrderQueryType.END, partnersEndOrderQueryService());
        map.put(PartnersOrderQueryType.CANCEL, partnersCancelOrderQueryService());

        return map;
    }

    @Bean
    public PartnersReadyOrderQueryServiceImpl partnersReadyOrderQueryService() {
        return new PartnersReadyOrderQueryServiceImpl(repository);
    }

    @Bean
    public PartnersDeliveryOrderQueryServiceImpl partnersDeliveryOrderQueryService() {
        return new PartnersDeliveryOrderQueryServiceImpl(repository);
    }

    @Bean
    public PartnersEndOrderQueryServiceImpl partnersEndOrderQueryService() {
        return new PartnersEndOrderQueryServiceImpl(repository);
    }

    @Bean
    public PartnersCancelOrderQueryServiceImpl partnersCancelOrderQueryService() {
        return new PartnersCancelOrderQueryServiceImpl(repository);
    }
}
