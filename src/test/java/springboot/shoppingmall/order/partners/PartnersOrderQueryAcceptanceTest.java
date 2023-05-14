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
     *  given: 상품 준비 중인 주문과 상품이 출고된 주문이 있음
     *  And: 판매자가 로그인 되어 있음
     *  when: 배송 준비중인 주문 목록을 조회하면
     *  then: 상품 준비 중인 주문과 상품이 출고된 주문이 조회된다.
     */
    @Test
    @DisplayName("판매자가 배송 준비중 / 상품 출고 상태의 주문 목록을 조회한다.")
    void partners_order_query_find_ready() {
        // given
        OrderResponse 주문1 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2 = 주문_생성_요청(상품2, 10, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2_출고중 = 주문_출고중_요청(주문2).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 판매자_준비중_주문_조회_결과 =
                판매자_주문내역_조회_요청(PartnersOrderQueryType.READY);

        // then
        assertThat(판매자_준비중_주문_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.id", Long.class,
                주문1.getId(), 주문2.getId()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.productName", String.class,
                상품.getName(), 상품2.getName()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.quantity", Integer.class,
                주문1.getQuantity(), 주문2.getQuantity()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.totalPrice", Integer.class,
                주문1.getTotalPrice(), 주문2.getTotalPrice()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.userName", String.class,
                인수테스터1.getName(), 인수테스터1.getName()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.receiverName", String.class,
                배송지.getReceiverName(), 배송지.getReceiverName()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.address", String.class,
                배송지.getAddress(), 배송지.getAddress()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.detailAddress", String.class,
                배송지.getDetailAddress(), 배송지.getDetailAddress()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.requestMessage", String.class,
                배송지.getRequestMessage(), 배송지.getRequestMessage()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.orderStatusName", String.class,
                OrderStatus.READY.getStatusName(), OrderStatus.OUTING.getStatusName()
        );
        목록_조회_결과_검증(판매자_준비중_주문_조회_결과, "data.invoiceNumber", String.class,
                null, 주문2_출고중.getInvoiceNumber()
        );
    }

    /**
     *  given: 배송중인 주문이 있다.
     *  And: 판매자가 로그인 되어 있음
     *  when: 배송 중인 주문 목록을 조회하면
     *  then: 배송 중인 주문내역이 조회된다.
     */
    @Test
    @DisplayName("판매자가 배송중인 주문 내역을 조회한다.")
    void partners_find_order_delivery() {
        // given
        OrderResponse 주문1 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2 = 주문_생성_요청(상품2, 10, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문1_출고중 = 주문_출고중_요청(주문1).as(OrderResponse.class);
        OrderResponse 주문2_출고중 = 주문_출고중_요청(주문2).as(OrderResponse.class);
        OrderResponse 주문1_배송중 = 주문_배송중_요청(주문1_출고중).as(OrderResponse.class);
        OrderResponse 주문2_배송중 = 주문_배송중_요청(주문2_출고중).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 판매자_주문내역_조회_결과 =
                판매자_주문내역_조회_요청(PartnersOrderQueryType.DELIVERY);

        // then
        assertThat(판매자_주문내역_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.id", Long.class, 주문1_배송중.getId(), 주문2_배송중.getId());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.address", String.class, 배송지.getAddress(), 배송지.getAddress());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.detailAddress", String.class, 배송지.getDetailAddress(), 배송지.getDetailAddress());
    }

    /**
     *  given: 배송이 완료된 주문이 있다.
     *  And: 판매자가 로그인 되어 있음
     *  when: 배송완료인 주문 목록을 조회하면
     *  then: 배송이 완료된 주문내역이 조회된다.
     */
    @Test
    @DisplayName("판매자가 배송이 완려된 주문 내역을 조회한다.")
    void partners_find_order_end() {
        // given
        OrderResponse 주문1 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2 = 주문_생성_요청(상품2, 10, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문3 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문1_출고중 = 주문_출고중_요청(주문1).as(OrderResponse.class);
        OrderResponse 주문3_출고중 = 주문_출고중_요청(주문3).as(OrderResponse.class);
        OrderResponse 주문1_배송중 = 주문_배송중_요청(주문1_출고중).as(OrderResponse.class);
        OrderResponse 주문3_배송중 = 주문_배송중_요청(주문3_출고중).as(OrderResponse.class);
        OrderResponse 주문1_배송완료 = 주문_배송완료_요청(주문1_배송중).as(OrderResponse.class);
        OrderResponse 주문3_배송완료 = 주문_배송완료_요청(주문3_배송중).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 판매자_주문내역_조회_결과 =
                판매자_주문내역_조회_요청(PartnersOrderQueryType.END);

        // then
        assertThat(판매자_주문내역_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.id", Long.class, 주문1_배송완료.getId(), 주문3_배송완료.getId());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.address", String.class, 배송지.getAddress(), 배송지.getAddress());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.detailAddress", String.class, 배송지.getDetailAddress(), 배송지.getDetailAddress());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.deliveryDate", String.class, "need", "need");
    }

    /**
     *  given: 구매자가 취소한 주문이 있다.
     *  And: 구매자가 환불을 요청한 주문이 있다.
     *  And: 구매자가 환불받은 주문이 있다.
     *  And: 구매자가 교환을 요청한 주문이 있다.
     *  And: 구매자가 교환을 요청하여 상품을 검수 중인 주문이 있다.
     *  And: 구매자가 환불을 요청하여 상품을 검수 중인 주문이 있다.
     *  And: 판매자가 로그인 되어 있음
     *  when: 배송완료인 주문 목록을 조회하면
     *  then: 배송이 완료된 주문내역이 조회된다.
     */
    @Test
    @DisplayName("판매자가 배송중인 주문 내역을 조회한다.")
    void partners_find_order_cancel() {
        // given
        OrderResponse 주문1 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문1_주문취소 = 주문_주문취소_요청(주문1).as(OrderResponse.class);

        OrderResponse 주문2 = 주문_생성_요청(상품2, 10, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문2_출고중 = 주문_출고중_요청(주문2).as(OrderResponse.class);
        OrderResponse 주문2_배송중 = 주문_배송중_요청(주문2_출고중).as(OrderResponse.class);
        OrderResponse 주문2_배송완료 = 주문_배송완료_요청(주문2_배송중).as(OrderResponse.class);
        OrderResponse 주문2_환불요청 = 주문_환불_요청(주문2_배송완료, "상품이 불량같아 환불을 요청드립니다.").as(OrderResponse.class);

        OrderResponse 주문3 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문3_출고중 = 주문_출고중_요청(주문3).as(OrderResponse.class);
        OrderResponse 주문3_배송중 = 주문_배송중_요청(주문3_출고중).as(OrderResponse.class);
        OrderResponse 주문3_배송완료 = 주문_배송완료_요청(주문3_배송중).as(OrderResponse.class);
        OrderResponse 주문3_환불요청 = 주문_환불_요청(주문3_배송완료, "상품이 불량같아 환불을 요청드립니다.").as(OrderResponse.class);
        OrderResponse 주문3_환불완료 = 주문_환불완료_요청(주문3_환불요청).as(OrderResponse.class);

        OrderResponse 주문4 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문4_출고중 = 주문_출고중_요청(주문4).as(OrderResponse.class);
        OrderResponse 주문4_배송중 = 주문_배송중_요청(주문4_출고중).as(OrderResponse.class);
        OrderResponse 주문4_배송완료 = 주문_배송완료_요청(주문4_배송중).as(OrderResponse.class);
        OrderResponse 주문4_교환요청 = 주문_교환_요청(주문4_배송완료, "사이즈가 안맞아서 교환 요청합니다.").as(OrderResponse.class);

        OrderResponse 주문5 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문5_출고중 = 주문_출고중_요청(주문5).as(OrderResponse.class);
        OrderResponse 주문5_배송중 = 주문_배송중_요청(주문5_출고중).as(OrderResponse.class);
        OrderResponse 주문5_배송완료 = 주문_배송완료_요청(주문5_배송중).as(OrderResponse.class);
        OrderResponse 주문5_교환요청 = 주문_교환_요청(주문5_배송완료, "사이즈가 안맞아서 교환 요청합니다.").as(OrderResponse.class);

        OrderResponse 주문6 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문6_출고중 = 주문_출고중_요청(주문6).as(OrderResponse.class);
        OrderResponse 주문6_배송중 = 주문_배송중_요청(주문6_출고중).as(OrderResponse.class);
        OrderResponse 주문6_배송완료 = 주문_배송완료_요청(주문6_배송중).as(OrderResponse.class);
        OrderResponse 주문6_교환요청 = 주문_교환_요청(주문6_배송완료, "사이즈가 안맞아서 교환 요청합니다.").as(OrderResponse.class);
        OrderResponse 주문6_검수중 = 주문_검수중_요청(주문6_교환요청).as(OrderResponse.class);

        OrderResponse 주문7 = 주문_생성_요청(상품, 3, 0, 배송지).as(OrderResponse.class);
        OrderResponse 주문7_출고중 = 주문_출고중_요청(주문7).as(OrderResponse.class);
        OrderResponse 주문7_배송중 = 주문_배송중_요청(주문7_출고중).as(OrderResponse.class);
        OrderResponse 주문7_배송완료 = 주문_배송완료_요청(주문7_배송중).as(OrderResponse.class);
        OrderResponse 주문7_교환요청 = 주문_환불_요청(주문7_배송완료, "사이즈가 안맞아서 교환 요청합니다.").as(OrderResponse.class);
        OrderResponse 주문7_검수중 = 주문_검수중_요청(주문7_교환요청).as(OrderResponse.class);


        // when
        ExtractableResponse<Response> 판매자_주문내역_조회_결과 =
                판매자_주문내역_조회_요청(PartnersOrderQueryType.CANCEL);

        // then
        assertThat(판매자_주문내역_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        목록_조회_결과_검증(판매자_주문내역_조회_결과, "data.id", Long.class,
                주문1.getId(), 주문2.getId(), 주문3.getId(), 주문4.getId(), 주문5.getId(), 주문6.getId(), 주문7.getId()
        );
    }

    private ExtractableResponse<Response> 판매자_주문내역_조회_요청(PartnersOrderQueryType type) {
        LocalDateTime now = LocalDateTime.now();
        String startDateTimeParam = DateUtils.toStringOfLocalDateTIme(now.minusMinutes(3), "yyyy-MM-dd");
        String endDateTimeParam = DateUtils.toStringOfLocalDateTIme(now, "yyyy-MM-dd");

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/orders?type={type}&startDate={startDate}&endDate={endDate}",
                        type,
                        startDateTimeParam,
                        endDateTimeParam)
                .then().log().all()
                .extract();
    }
}
