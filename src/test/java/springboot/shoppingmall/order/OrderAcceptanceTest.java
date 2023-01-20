package springboot.shoppingmall.order;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;
import static springboot.shoppingmall.user.DeliveryAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.user.dto.DeliveryResponse;

public class OrderAcceptanceTest extends AcceptanceTest {

    ProductResponse 상품;
    DeliveryResponse 배송지;

    @BeforeEach
    void setUp() {
        super.beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        배송지 = 배송지_추가_요청("배송지 1",
                "수령인 1",
                "10010",
                "서울시 서초구 서초동 103번지",
                "109호",
                "부재 시, 경비실에 놔주세요.").as(DeliveryResponse.class);
    }

    /**
     * Feature: 최초 주문
     *  Background:
     *      given: 로그인한 사용자
     *      And: 등록되어있는 상품
     *      And: 사용자가 등록해놓은 배송지 정보가 존재함
     *
     *  Scenario: 최초 주문 생성
     *      when: 구매하고자 하는 상품정보와  갯수, 배송지 정보를 가지고 주문을 생성하면
     *      then: 주문 내역 조회 시, 주문된 내용이 조회된다.
     */
    @Test
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        // given

        // when
        ExtractableResponse<Response> 주문_생성_결과 = 주문_생성_요청(상품, 3, 3000, 배송지);

        // then
        assertThat(주문_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(주문_생성_결과.jsonPath().getLong("id")).isNotNull();
    }

    /**
     * Feature: 준비중인 주문의 상태를 출고중으로 변경
     *  Background:
     *      given: 판매자 정보로 로그인 되어있음
     *      And: 판매자가 판매한 상품의 준비중인 주문이 들어와 있음
     *  Scenario: 준비중인 주문의 상태을 출고중으로 변경
     *      when: 조회된 주문정보의 주문상태를 출고중으로 변경하면
     *      then: 주문정보가 출고중으로 변경된다.
     */
    @Test
    @DisplayName("주문 상태 변경 : 준비중 -> 출고중")
    void statusChangeTest1(){
        // given
        OrderResponse 주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_상태_변경_요청(주문, OrderStatus.OUTING); 

        // then
        assertThat(주문_상태_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Feature: 주문 상태 변경 테스트
     *  Background:
     *      given: 판매자 정보로 로그인 되어있음
     *      And: 판매자가 판매한 상품의 준비중인 주문이 들어와 있음
     *  Scenario: 주문 상태 변경 시나리오
     *      when: 준비 중인 주문을 출고중으로 변경하면
     *      then: 주문 상태가 출고중으로 변경되고,
     *      when: 출고중인 주문 상태를 주문 취소로 변경하면,
     *      then: 준비 중인 주문이 아니므로, 주문 취소 상태로 변경되지 않고,
     *      when: 출고중인 주문 상태를 배송 중으로 변경하면,
     *      then: 주문 상태가 배송중으로 변경된다.
     */
    @Test
    @DisplayName("주문 상태 변경 시나리오")
    void statusChangeTest(){
        // given
        OrderResponse 준비중_주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);
        assertThat(준비중_주문.getOrderStatusName()).isEqualTo(OrderStatus.READY.getStatusName());

        // when : 준비 중인 주문을 출고중으로 변경하면
        ExtractableResponse<Response> 준비중_에서_출고중_변경_결과 = 주문_상태_변경_요청(준비중_주문, OrderStatus.OUTING);
        assertThat(준비중_에서_출고중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 출고중으로 변경되고
        OrderResponse 출고중_주문 = 준비중_에서_출고중_변경_결과.as(OrderResponse.class);
        assertThat(출고중_주문.getOrderStatusName()).isEqualTo(OrderStatus.OUTING.getStatusName());

        // when : 출고중인 주문 상태를 주문 취소로 변경하면
        ExtractableResponse<Response> 출고중_에서_주문취소_변경_결과 = 주문_상태_변경_요청(출고중_주문, OrderStatus.CANCEL);

        // then : 준비 중인 주문이 아니므로, 주문 취소 상태로 변경되지 않고
        assertThat(출고중_에서_주문취소_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // when : 출고중인 주문 상태를 배송 중으로 변경하면
        ExtractableResponse<Response> 출고중_에서_배송중_변경_결과 = 주문_상태_변경_요청(출고중_주문, OrderStatus.DELIVERY);
        assertThat(출고중_에서_배송중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 배송중으로 변경된다
        OrderResponse 배송중_주문 = 출고중_에서_배송중_변경_결과.as(OrderResponse.class);
        assertThat(배송중_주문.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(OrderResponse order, OrderStatus status) {
        Map<String, String> params = new HashMap<>();
        params.put("status", status.name());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/orders/{id}/change-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(ProductResponse product, int quantity, int deliveryFee, DeliveryResponse delivery) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", product.getId());
        params.put("quantity", quantity);
        params.put("deliveryFee", deliveryFee);
        params.put("deliveryId", delivery.getId());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/orders")
                .then().log().all()
                .extract();
    }
}
