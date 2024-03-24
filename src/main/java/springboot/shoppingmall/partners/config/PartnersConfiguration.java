package springboot.shoppingmall.partners.config;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.shoppingmall.partners.authentication.LoginPartnerArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class PartnersConfiguration implements WebMvcConfigurer {

    private final LoginPartnerArgumentResolver loginPartnerArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginPartnerArgumentResolver);
    }

}
