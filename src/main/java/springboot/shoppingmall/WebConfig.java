package springboot.shoppingmall;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.shoppingmall.authorization.GatewayAuthenticationArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GatewayAuthenticationArgumentResolver gatewayAuthenticationArgumentResolver;

    private static final String[] WHITE_LIST = {
            "http://localhost:3000", "http://local.shoppingmall.com:3000",
            "http://localhost:3001", "http://local.shoppingmall.com:3001"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(WHITE_LIST)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
//                .maxAge(1800); // 30min
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(gatewayAuthenticationArgumentResolver);
    }
}
