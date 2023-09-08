package springboot.shoppingmall.payment;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.payment.presentation.response.PaymentResponse;

public class PaymentAcceptanceTest extends AcceptanceTest {

    String cardNo1 = "1234";
    String cardNo2 = "5678";
    String cardNo3 = "9012";
    String cardNo4 = "3456";
    String expireMM = "09";
    String expireYY = "27";
    String cvc = "123";
    String cardCom = "SS";
    String payType = "CARD";

    /**
     *  given: 사용자가 로그인 되어 있음
     *  when: 결제정보를 입력하면
     *  then: 결제정보가 추가된다.
     */
    @Test
    @DisplayName("결제수단 추가 테스트")
    void createPaymentTest(){
        // given

        // when
        ExtractableResponse<Response> 결제수단_추가_결과 = 결제수단_추가_요청(cardNo1, cardNo2, cardNo3, cardNo4, expireMM, expireYY, cvc, cardCom, payType);

        // then
        assertThat(결제수단_추가_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        PaymentResponse paymentResponse = 결제수단_추가_결과.as(PaymentResponse.class);
        assertThat(paymentResponse.getId()).isNotNull();
    }

    /**
     * given: 로그인된 사용자에 결제수단이 등록되어 있음
     * when: 결제수단 삭제를 시도하면
     * then: 삭제 요청을 보낸 사용자의 결제수단이 맞다면, 결제수단을 삭제한다.
     *
     */
    @Test
    @DisplayName("결제수단 삭제 테스트")
    void deletePayment() {
        // given
        PaymentResponse 추가된_결제수단 = 결제수단_추가_요청(cardNo1, cardNo2, cardNo3, cardNo4, expireMM, expireYY, cvc, cardCom, payType)
                .as(PaymentResponse.class);

        // when
        ExtractableResponse<Response> 결제수단_삭제_결과 = 결제수단_삭제_요청(추가된_결제수단);

        // then
        assertThat(결제수단_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * given: 등록된 결제수단이 미리 존재하고
     * when: 결제수단 목록을 조회하면
     * then: 등록된 결제수단 목록이 조회된다.
     */
    @Test
    @DisplayName("결제수단 목록 조회 테스트")
    void findAllPaymentTest() {
        // given
        결제수단_추가_요청(cardNo1, cardNo2, cardNo3, cardNo4, expireMM, expireYY, cvc, cardCom, payType);
        결제수단_추가_요청("9999", cardNo2, cardNo3, cardNo4, expireMM, expireYY, cvc, cardCom, payType);

        // when
        ExtractableResponse<Response> 결제수단_목록_조회_결과 = 결제수단_목록_조회_요청();

        // then
        assertThat(결제수단_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(결제수단_목록_조회_결과.jsonPath().getList("id")).hasSize(2);
    }

    private ExtractableResponse<Response> 결제수단_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/payments")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 결제수단_삭제_요청(PaymentResponse paymentResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().delete("/payments/{id}", paymentResponse.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 결제수단_추가_요청(String cardNo1, String cardNo2, String cardNo3, String cardNo4,
                                                     String expireMM, String expireYY, String cvc, String cardCom, String payType) {
        Map<String, String> params = new HashMap<>();
        params.put("cardNo1", cardNo1);
        params.put("cardNo2", cardNo2);
        params.put("cardNo3", cardNo3);
        params.put("cardNo4", cardNo4);
        params.put("expireMM", expireMM);
        params.put("expireYY", expireYY);
        params.put("cvc", cvc);
        params.put("payType", payType);
        params.put("cardCom", cardCom);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/payments")
                .then().log().all()
                .extract();
    }
}
