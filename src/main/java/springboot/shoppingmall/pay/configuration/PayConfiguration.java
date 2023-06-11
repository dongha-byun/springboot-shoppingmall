package springboot.shoppingmall.pay.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.type.card.service.CardPayService;
import springboot.shoppingmall.pay.type.kakakopay.service.KakaoPayService;
import springboot.shoppingmall.pay.service.PayService;

@Configuration
public class PayConfiguration {

    @Bean
    public PayService payService() {
        return new KakaoPayService(restTemplate());
    }

    @Bean
    public KakaoPayService kakaoPayService() {
        return new KakaoPayService(restTemplate());
    }

    @Bean
    public CardPayService cardPayService() {
        return new CardPayService();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
