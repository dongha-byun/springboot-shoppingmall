package springboot.shoppingmall.order.partners.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.service.PartnersCancelOrderQueryServiceImpl;
import springboot.shoppingmall.order.partners.service.PartnersOrderQueryServiceInterface;

@SpringBootTest
class PartnersOrderQueryControllerTest {

    @Autowired
    Map<PartnersOrderQueryType, PartnersOrderQueryServiceInterface> partnersOrderQueryServiceMap;

    @Test
    @DisplayName("type 별 Service 조회")
    void type_service_test() {
        // given

        // when
        PartnersOrderQueryServiceInterface partnersOrderQueryService =
                partnersOrderQueryServiceMap.get(PartnersOrderQueryType.CANCEL);

        // then
        assertThat(partnersOrderQueryService).isInstanceOf(
                PartnersCancelOrderQueryServiceImpl.class
        );
    }
}