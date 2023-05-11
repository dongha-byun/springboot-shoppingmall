package springboot.shoppingmall.order.partners;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.order.OrderAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceProductTest;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.utils.DateUtils;

public class PartnersOrderQueryAcceptanceTest extends AcceptanceProductTest {

    /**
     *  given: 배송 준비중인 상품이 있음
     *  And: 판매자가 로그인 되어 있음
     *  when: 배송 준비중인 상품 목록을 조회하면
     *  then: 배송 준비중인 상품들이 조회된다.
     */
    @Test
    @DisplayName("판매자가 배송 준비중 상태의 주문 목록을 조회한다.")
    void partners_order_query_find_ready() {
        // given
        OrderResponse 주문1 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2 = 주문_생성_요청(상품2, 10, 0, 배송지).as(OrderResponse.class);
        주문_출고중_요청(주문2);

        LocalDateTime now = LocalDateTime.now();
        String startDateTimeParam = DateUtils.toStringOfLocalDateTIme(now.minusMinutes(3), "yyyy-MM-dd");
        String endDateTimeParam = DateUtils.toStringOfLocalDateTIme(now, "yyyy-MM-dd");

        // when
        ExtractableResponse<Response> 판매자_준비중_주문_조회_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/orders/{type}?startDate={startDate}&endDate={endDate}",
                        PartnersOrderQueryType.READY,
                        startDateTimeParam,
                        endDateTimeParam)
                .then().log().all()
                .extract();

        // then
        assertThat(판매자_준비중_주문_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.productName", String.class)).containsExactly(
                상품.getName(), 상품2.getName()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.quantity", Integer.class)).containsExactly(
                주문1.getQuantity(), 주문2.getQuantity()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.totalPrice", Integer.class)).containsExactly(
                주문1.getTotalPrice(), 주문2.getTotalPrice()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.userName", String.class)).containsExactly(
                인수테스터1.getName(), 인수테스터1.getName()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.receiverName", String.class)).containsExactly(
                배송지.getReceiverName(), 배송지.getReceiverName()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.address", String.class)).containsExactly(
                배송지.getAddress(), 배송지.getAddress()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.detailAddress", String.class)).containsExactly(
                배송지.getDetailAddress(), 배송지.getDetailAddress()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.requestMessage", String.class)).containsExactly(
                배송지.getRequestMessage(), 배송지.getRequestMessage()
        );
        assertThat(판매자_준비중_주문_조회_결과.jsonPath().getList("data.orderStatusName", String.class)).containsExactly(
                OrderStatus.READY.getStatusName(), OrderStatus.OUTING.getStatusName()
        );
    }
}
