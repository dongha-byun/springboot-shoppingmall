package springboot.shoppingmall;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.shoppingmall.userservice.authentication.email.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.userservice.authentication.email.domain.MailNotifier;
import springboot.shoppingmall.userservice.authentication.email.domain.MemoryEmailAuthorizationCodeStore;


@TestConfiguration
public class TestEmailAuthorizationConfig {

    @Bean
    public AuthorizationCodeGenerator codeGenerator() {
        return () -> "012345";
    }

    @Bean
    public EmailAuthorizationCodeStore store() {
        return new MemoryEmailAuthorizationCodeStore();
    }

    @Bean
    public MailNotifier mailNotifier() {
        return (email, title, message) -> {
            // .... 아무것도 안해도 되긴하는데....
        };
    }
}
