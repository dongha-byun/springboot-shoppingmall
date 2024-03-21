package springboot.shoppingmall.order.partners.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.partners.application.PartnersCancelOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersDeliveryOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersEndOrderQueryService;
import springboot.shoppingmall.order.partners.application.PartnersReadyOrderQueryService;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;

@RequiredArgsConstructor
@Configuration
public class PartnersOrderQueryConfiguration {

    private final PartnersOrderQueryRepository repository;
    private final OrderUserInterfaceService orderUserInterfaceService;

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
