package springboot.shoppingmall;

import io.jsonwebtoken.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.shoppingmall.interceptor.SessionCheckInterceptor;
import springboot.shoppingmall.utils.login.JwtTokenProvider;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("http://localhost:3000/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(1800); // 30min

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/*.ico");

    }

    @Bean
    public SessionCheckInterceptor sessionCheckInterceptor(){
        return new SessionCheckInterceptor();
    }
}
