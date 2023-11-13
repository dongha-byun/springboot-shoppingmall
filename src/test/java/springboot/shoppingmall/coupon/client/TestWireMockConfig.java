package springboot.shoppingmall.coupon.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestWireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockUserMicroService() {
        return new WireMockServer(options().port(8881));
    }

}
