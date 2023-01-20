package springboot.shoppingmall.delivery;

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
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceResponse;

public class DeliveryAcceptanceTest extends AcceptanceTest {

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
