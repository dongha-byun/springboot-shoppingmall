package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.DeliveryResponse;
import springboot.shoppingmall.user.dto.DeliveryRequest;

@Transactional
@SpringBootTest
class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    UserRepository userRepository;
    User saveUser;

    @BeforeEach
    void beforeEach(){
        saveUser = userRepository.save(User.builder()
                .userName("테스터1")
                .email("tester1@test.com")
                .password("tester1!")
                .telNo("010-2222-3333")
                .build());
    }

    @Test
    @DisplayName("배송지 정보 저장 테스트")
    void createTest(){
        // given
        DeliveryRequest deliveryRequest = new DeliveryRequest(
                "닉네임 1", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );

        // when
        DeliveryResponse deliveryResponse = deliveryService.create(saveUser.getId(), deliveryRequest);

        // then
        assertThat(deliveryResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("배송지 정보 삭제 테스트")
    void deleteTest(){
        // given
        DeliveryRequest deliveryRequest1 = new DeliveryRequest(
                "닉네임 1", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );
        DeliveryRequest deliveryRequest2 = new DeliveryRequest(
                "닉네임 2", "수령인 1", "010-1234-1234",
                "11002", "서울시 영등포구 당산동", "1동 2호", ""
        );
        deliveryService.create(saveUser.getId(), deliveryRequest1);
        DeliveryResponse deliveryResponse = deliveryService.create(saveUser.getId(), deliveryRequest2);

        // when
        deliveryService.delete(saveUser.getId(), deliveryResponse.getId());

        // then
        List<DeliveryResponse> deliveries = deliveryService.findAllDelivery(saveUser.getId());
        assertThat(deliveries).hasSize(1);
    }
}