package springboot.shoppingmall.coupon.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.coupon.application.dto.ResponseUserInformation;

@RequiredArgsConstructor
public class RestUserCouponService {
    private final RestTemplate restTemplate;

    public List<Long> getUserIdsAboveTheGrade(String targetGrade) {
        ResponseEntity<List<Long>> exchange = restTemplate.exchange(
                "/users/aboveGrade?targetGrade=" + targetGrade,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }
        );
        return exchange.getBody();
    }

    public List<ResponseUserInformation> getUsers(List<Long> userIds) {
        RequestEntity<List<Long>> requestEntity = RequestEntity
                .post("/users/has-coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userIds);

        ResponseEntity<List<ResponseUserInformation>> response = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
        List<ResponseUserInformation> result = response.getBody();
        if(result == null) {
            throw new IllegalArgumentException("사용자 정보 조회 실패");
        }

        return result;
    }
}
