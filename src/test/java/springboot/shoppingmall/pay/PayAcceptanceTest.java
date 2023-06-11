package springboot.shoppingmall.pay;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayReadyRequest;
import springboot.shoppingmall.pay.web.PayRequest;

public class PayAcceptanceTest extends AcceptanceTest {

    String cid = "TC0ONETIME";
    String partner_order_id = "dongha_shopping_mall_order";
    String partner_user_id = "TuserId";
    String item_name = "TitemName";
    int quantity = 10;
    int total_amount = 11000;
    int vat_amount = 1000;
    int tax_free_amount = 0;
    String approval_url = "http://localhost:10000/pay/approve";
    String fail_url = "http://localhost:10000/pay/fail";
    String cancel_url = "http://localhost:10000/pay/cancel";

    /**
     * given: 결제 정보를 포함하여
     * when: 카카오페이 결제준비 API 를 호출하면
     * then: 결제를 위한 URL 을 받는다.
     */
    @Test
    @DisplayName("카카오 페이 결제 준비 테스트")
    void kakao_pay_ready_test() {
        // given
        KakaoPayReadyRequest kakaoPayReadyRequest =
                new KakaoPayReadyRequest(cid, partner_order_id, partner_user_id, item_name, quantity,
                        total_amount, vat_amount, tax_free_amount, approval_url, fail_url, cancel_url);
        PayRequest<KakaoPayReadyRequest> request =
                new PayRequest<>("kakaoPay", kakaoPayReadyRequest);

        // when
        ExtractableResponse<Response> 카카오페이_결제준비_요청_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/pay/ready")
                .then().log().all()
                .extract();

        // then
        assertThat(카카오페이_결제준비_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
