package springboot.shoppingmall.user;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.order.OrderAcceptanceTest.주문_생성_요청;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;
import static springboot.shoppingmall.user.DeliveryAcceptanceTest.배송지_추가_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.user.dto.DeliveryResponse;

public class OrderHistoryAcceptanceTest extends AcceptanceTest {

    ProductResponse 상품;
    DeliveryResponse 배송지;

    @BeforeEach
    void setUp() {
        super.acceptance_beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        배송지 = 배송지_추가_요청("배송지 1",
                "수령인 1",
                "010-1234-1234",
                "10010",
                "서울시 서초구 서초동 103번지",
                "109호",
                "부재 시, 경비실에 놔주세요.").as(DeliveryResponse.class);
    }

    /**
     *  Background:
     *      given: 로그인한 사용자
     *      And: 추가된 상품
     *  Scenario: 주문 내역 조회
     *      given: 사용자가 상품을 주문하고
     *      when: 주문내역 조회 시
     *      then: 주문한 상품이 조회된다.
     */
    @Test
    @DisplayName("주문 내역 조회 - 구매자")
    void orderHistoryTest(){
        // given
        LocalDateTime endLocalDateTime = LocalDateTime.now();
        LocalDateTime startLocalDateTime = endLocalDateTime.minusMonths(3);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String startDate = startLocalDateTime.format(dateTimeFormatter);
        String endDate = endLocalDateTime.format(dateTimeFormatter);

        OrderResponse 주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 개인_주문_목록_조회_결과 = 개인_주문_목록_조회_요청(startDate, endDate);

        // then
        assertThat(개인_주문_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(개인_주문_목록_조회_결과.jsonPath().getList("orderId")).hasSize(1);
        assertThat(개인_주문_목록_조회_결과.jsonPath().getList("orderItemId", Long.class)).hasSize(1);
        assertThat(개인_주문_목록_조회_결과.jsonPath().getList("orderItemId", Long.class)).containsExactly(
                주문.getItems().get(0).getId()
        );
        assertThat(개인_주문_목록_조회_결과.jsonPath().getList("productId")).hasSize(1);
        assertThat(개인_주문_목록_조회_결과.jsonPath().getList("productId", Long.class)).containsExactly(
                상품.getId()
        );
    }

    private ExtractableResponse<Response> 개인_주문_목록_조회_요청(String startDate, String endDate) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/user/orders"
                                + "?startDate={startDate}"
                                + "&endDate={endDate}",
                        startDate,
                        endDate)
                .then().log().all()
                .extract();
    }
}
