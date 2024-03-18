package springboot.shoppingmall;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.shoppingmall.authorization.GatewayAuthenticationArgumentResolver;
import springboot.shoppingmall.common.search.SearchEndDateArgumentResolver;
import springboot.shoppingmall.common.search.SearchStartDateArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GatewayAuthenticationArgumentResolver gatewayAuthenticationArgumentResolver;
    private final SearchStartDateArgumentResolver searchStartDateArgumentResolver;
    private final SearchEndDateArgumentResolver searchEndDateArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(gatewayAuthenticationArgumentResolver);
        resolvers.add(searchStartDateArgumentResolver);
        resolvers.add(searchEndDateArgumentResolver);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
