package springboot.shoppingmall.coupon.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.coupon.client.UserCouponService;

@RequiredArgsConstructor
@Component
public class RestUserCouponService implements UserCouponService {
    private final RestTemplate restTemplate;

    @Override
    public List<Long> getUserIdsAboveTheGrade(String targetGrade) {
        ResponseEntity<List<Long>> exchange = restTemplate.exchange(
                "/users/aboveGrade?targetGrade=" + targetGrade,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Long>>() {
                }
        );
        return exchange.getBody();
    }
}
