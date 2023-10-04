package springboot.shoppingmall.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.order.application.dto.RequestUserOrderAmount;
import springboot.shoppingmall.order.application.dto.ResponseUserInformation;
import springboot.shoppingmall.order.application.dto.ResponseUserOrderAmount;

@RequiredArgsConstructor
@Component
public class RestOrderUserInterfaceService implements OrderUserInterfaceService {

    private final RestTemplate restTemplate;

    @Override
    public int getOrderUserDiscountRate(Long userId) {
        ResponseUserInformation userInformation =
                restTemplate.getForObject("/user/{userId}/grade-info", ResponseUserInformation.class, userId);

        if(userInformation == null) {
            throw new IllegalArgumentException("사용자 조회 실패. 잠시 후, 다시 시도해주세요.");
        }
        return userInformation.getDiscountRate();
    }

    @Override
    public void increaseOrderAmounts(Long userId, int price) {
        RequestUserOrderAmount request = new RequestUserOrderAmount(price);
        restTemplate.patchForObject(
                "/users/{userId}/order-amounts", request, ResponseUserOrderAmount.class, userId
        );
    }
}
