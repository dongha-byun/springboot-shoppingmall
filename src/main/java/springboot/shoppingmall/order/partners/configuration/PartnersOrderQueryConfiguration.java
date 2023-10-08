package springboot.shoppingmall.order.partners.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;

@RequiredArgsConstructor
@Configuration
public class PartnersOrderQueryConfiguration {

    private final PartnersOrderQueryRepository repository;
    private final OrderUserInterfaceService orderUserInterfaceService;

//    public Map<PartnersOrderQueryType, PartnersOrderQueryService> partnersOrderQueryServiceMap() {
//        Map<PartnersOrderQueryType, PartnersOrderQueryService> map = new HashMap<>();
//        map.put(PartnersOrderQueryType.READY, partnersReadyOrderQueryService());
//        map.put(PartnersOrderQueryType.DELIVERY, partnersDeliveryOrderQueryService());
//        map.put(PartnersOrderQueryType.END, partnersEndOrderQueryService());
//        map.put(PartnersOrderQueryType.CANCEL, partnersCancelOrderQueryService());
//
//        return map;
//    }

    @Bean
    public PartnersReadyOrderQueryService partnersReadyOrderQueryService() {
        return new PartnersReadyOrderQueryService(repository, orderUserInterfaceService);
    }

    @Bean
    public PartnersDeliveryOrderQueryService partnersDeliveryOrderQueryService() {
        return new PartnersDeliveryOrderQueryService(repository, orderUserInterfaceService);
    }

    @Bean
    public PartnersEndOrderQueryService partnersEndOrderQueryService() {
        return new PartnersEndOrderQueryService(repository, orderUserInterfaceService);
    }

    @Bean
    public PartnersCancelOrderQueryService partnersCancelOrderQueryService() {
        return new PartnersCancelOrderQueryService(repository, orderUserInterfaceService);
    }
}
