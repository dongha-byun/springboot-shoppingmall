package springboot.shoppingmall.delivery.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.delivery.application.DeliveryService;
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;
import springboot.shoppingmall.delivery.presentation.request.DeliveryRequest;

@Transactional
@SpringBootTest
class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;

    Long userId = 10L;


    @Test
    @DisplayName("사용자가 배송지 정보를 추가한다.")
    void create(){
        // given
        DeliveryRequest deliveryRequest = new DeliveryRequest(
                "닉네임 1", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );

        // when
        DeliveryResponse deliveryResponse = deliveryService.create(userId, deliveryRequest);

        // then
        assertThat(deliveryResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("사용자가 자신의 배송지 정보를 삭제한다.")
    void delete(){
        // given
        DeliveryRequest deliveryRequest1 = new DeliveryRequest(
                "닉네임 1", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );
        DeliveryRequest deliveryRequest2 = new DeliveryRequest(
                "닉네임 2", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );
        deliveryService.create(userId, deliveryRequest1);
        DeliveryResponse deliveryResponse = deliveryService.create(userId, deliveryRequest2);

        // when
        deliveryService.delete(userId, deliveryResponse.getId());

        // then
        List<DeliveryResponse> deliveries = deliveryService.findAllDelivery(userId);
        assertThat(deliveries).hasSize(1);
    }
}