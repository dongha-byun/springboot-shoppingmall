package springboot.shoppingmall;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.shoppingmall.authorization.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.authorization.domain.MailNotifier;
import springboot.shoppingmall.authorization.domain.MemoryEmailAuthorizationCodeStore;

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
