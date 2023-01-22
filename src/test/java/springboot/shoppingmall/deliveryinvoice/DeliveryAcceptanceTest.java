package springboot.shoppingmall.deliveryinvoice;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.order.OrderAcceptanceTest.*;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;
import static springboot.shoppingmall.user.DeliveryAcceptanceTest.배송지_추가_요청;

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
import springboot.shoppingmall.deliveryinvoice.dto.DeliveryInvoiceResponse;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.user.dto.DeliveryResponse;

public class DeliveryAcceptanceTest extends AcceptanceTest {

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
     *  given: 수령주소, 수령인, 판매업체 주소, 판매자정보 가 정해져있고
     *  when: 송장번호 발급을 요청하면
     *  then: 송장번호가 발급된다.
     */
    @Test
    @DisplayName("송장 번호 발부 테스트")
    void deliveryInvoiceTest(){
        // given
        String receiverName = "수령인";
        String receiverZipcode = "수령지 우편번호";
        String receiverAddress = "수령지 주소";
        String receiverDetailAddress = "수령지 상세주소";

        // when
        ExtractableResponse<Response> 송장번호_발급_결과 = 송장번호_발급_요청(receiverName, receiverZipcode, receiverAddress, receiverDetailAddress);
        assertThat(송장번호_발급_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        DeliveryInvoiceResponse 송장번호_정보 = 송장번호_발급_결과.as(DeliveryInvoiceResponse.class);
        assertThat(송장번호_정보.getInvoiceNumber()).isNotNull();
    }

    /**
     * given: 주문 상태가 배송중인 주문이 있고,
     * when: 송장번호와 함께 배송완료 상태를 전달하면
     * then: 주문 조회 시, 주문의 상태가 배송완료 상태가 된다.
     */
    @Test
    @DisplayName("배송완료 처리 테스트")
    void orderStatusEndTest(){
        // given
        OrderResponse 준비중_주문 = 주문_생성_요청(상품, 2, 0, 배송지).as(OrderResponse.class);
        OrderResponse 출고중_주문 = 주문_상태_변경_요청(준비중_주문, OrderStatus.OUTING).as(OrderResponse.class);
        OrderResponse 배송중_주문 = 주문_상태_변경_요청(출고중_주문, OrderStatus.DELIVERY).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 배송완료_처리_결과 = 배송완료_처리_요청(배송중_주문);

        // then
        assertThat(배송완료_처리_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 배송완료_처리_요청(OrderResponse order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/delivery-invoice/{invoiceNumber}/delivery-end", order.getInvoiceNumber())
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> 송장번호_발급_요청(String receiverName, String receiverZipcode, String receiverAddress,
                           String receiverDetailAddress) {
        Map<String, String> params = new HashMap<>();
        params.put("receiverName", receiverName);
        params.put("receiverZipcode", receiverZipcode);
        params.put("receiverAddress", receiverAddress);
        params.put("receiverDetailAddress", receiverDetailAddress);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/delivery-invoice")
                .then().log().all()
                .extract();
    }
}
