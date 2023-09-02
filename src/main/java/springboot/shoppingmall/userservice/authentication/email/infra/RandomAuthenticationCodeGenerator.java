package springboot.shoppingmall.userservice.authentication.email.infra;

import org.springframework.stereotype.Component;
import springboot.shoppingmall.userservice.authentication.email.domain.AuthenticationCodeGenerator;

@Component
public class RandomAuthenticationCodeGenerator implements AuthenticationCodeGenerator {

    @Override
    public String generate() {
        double randomValue = Math.random() * 100000;
        long randomLong = Math.round(randomValue);

        return String.format("%06d", randomLong);
    }
}
