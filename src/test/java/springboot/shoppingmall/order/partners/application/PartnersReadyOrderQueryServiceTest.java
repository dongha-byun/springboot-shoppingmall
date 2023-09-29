package springboot.shoppingmall.order.partners.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.partners.presentation.response.PartnersOrderQueryResponse;

@Transactional
@SpringBootTest
class PartnersReadyOrderQueryServiceTest {

    @Autowired
    PartnersReadyOrderQueryService service;

    // 판매자가 자신이 등록한 상품의 주문 중 상품준비중 이거나 상품이 출고된 주문정보를 조회한다.
    @Test
    @DisplayName("판매자가 ")
    void test() {
        // given

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 5, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 15, 23, 59, 59);
        List<PartnersOrderQueryResponse> partnersOrders = service.findPartnersOrders(100L, startDate, endDate);

        // then
        assertThat(partnersOrders).hasSize(3);
    }
}