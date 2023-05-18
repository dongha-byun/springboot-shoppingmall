package springboot.shoppingmall.pay.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.service.KakaoPayService;
import springboot.shoppingmall.pay.service.PayService;

@Configuration
public class PayConfiguration {

    @Bean
    public PayService payService() {
        return new KakaoPayService(restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
