package springboot.shoppingmall.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.order.service.dto.ResponseUserInformation;

@RequiredArgsConstructor
@Component
public class RestOrderUserInformationService implements OrderUserInformationService{

    private final RestTemplate restTemplate;

    @Override
    public int getOrderUserDiscountRate(Long userId) {
        ResponseEntity<ResponseUserInformation> result =
                restTemplate.exchange("/user/" + userId + "/grade-info",
                        HttpMethod.GET, null,
                        ResponseUserInformation.class);
        ResponseUserInformation userInformation = result.getBody();

        if(userInformation == null) {
            throw new IllegalArgumentException("사용자 조회 실패. 잠시 후, 다시 시도해주세요.");
        }
        return result.getBody().getDiscountRate();
    }
}
