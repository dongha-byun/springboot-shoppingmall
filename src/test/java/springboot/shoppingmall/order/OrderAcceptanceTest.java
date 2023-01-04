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

    private ExtractableResponse<Response> 주문_생성_요청(ProductResponse product, int quantity, int deliveryFee, DeliveryResponse delivery) {
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
