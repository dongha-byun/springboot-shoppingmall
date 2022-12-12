package springboot.shoppingmall.authorization.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.shoppingmall.authorization.argument.AuthenticationStrategyArgumentResolver;
import springboot.shoppingmall.authorization.service.AuthService;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {
    private final AuthService authService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(createAuthenticationStrategyArgumentResolver());

    }

    @Bean
    public AuthenticationStrategyArgumentResolver createAuthenticationStrategyArgumentResolver(){
        return new AuthenticationStrategyArgumentResolver(authService);
    }
}
