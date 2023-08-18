package springboot.shoppingmall.authorization.infra;

import org.springframework.stereotype.Component;
import springboot.shoppingmall.authorization.domain.AuthorizationCodeGenerator;

@Component
public class RandomAuthorizationCodeGenerator implements AuthorizationCodeGenerator {

    @Override
    public String generate() {
        double randomValue = Math.random() * 100000;
        long randomLong = Math.round(randomValue);

        return String.format("%06d", randomLong);
    }
}
