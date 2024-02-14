package springboot.shoppingmall.order.infra.rest;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.RequestUserOrderAmount;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.application.dto.ResponseUserInformation;
import springboot.shoppingmall.order.application.dto.ResponseUserOrderAmount;

@RequiredArgsConstructor
@Component
public class RestOrderUserInterfaceService implements OrderUserInterfaceService {
    private final RestTemplate restTemplate;

    @Override
    public void increaseOrderAmounts(Long userId, int price) {
        RequestUserOrderAmount request = new RequestUserOrderAmount(price);
        restTemplate.patchForObject(
                "/users/{userId}/order-amounts", request, ResponseUserOrderAmount.class, userId
        );
    }

    @Override
    public List<ResponseOrderUserInformation> getUsersOfOrders(List<Long> userIds) {
        RequestEntity<List<Long>> requestEntity = RequestEntity
                .post("/orders/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userIds);

        ResponseEntity<List<ResponseOrderUserInformation>> response = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
        List<ResponseOrderUserInformation> result = response.getBody();
        if(result == null) {
            throw new IllegalArgumentException("사용자 정보 조회 실패");
        }

        return result;
    }
}
