package springboot.shoppingmall.delivery;

import static org.assertj.core.api.Assertions.*;

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
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;

public class DeliveryAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setUp(){
        super.acceptance_beforeEach();
    }

    /**
     * Feature
     *  Background:
     *      given: 사용자 로그인 되어있음
     *
     *  Scenario: 배송지 관리
     *      when: 배송지 정보를 추가하면
     *      then: 배송지 정보 조회 시, 추가된 배송지 정보가 조회되고,
     *      when: 배송지 정보를 삭제하면
     *      then: 배송지 정보 조회 시, 삭제된 배송지 정보는 조회되지 않는다.
     */
    @Test
    @DisplayName("배송지 관리 테스트")
    void deliveryTest(){
        // given

        // when 1.
        ExtractableResponse<Response> 배송지_추가_요청_결과_1 =
                배송지_추가_요청(
                        "닉네임 1", "수령인 1", "010-1234-1234",
                        "10010", "서울시 강남구 청담동", "401동 1101호",
                        "부재 시, 경비실에 맡겨주세요."
                );
        ExtractableResponse<Response> 배송지_추가_요청_결과_2 =
                배송지_추가_요청(
                        "닉네임 2", "수령인 2", "010-2345-2345",
                        "20020", "서울시 영등포구 여의도동", "101동 101",
                        "조심히 오세요"
                );

        // then 1.
        assertThat(배송지_추가_요청_결과_1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(배송지_추가_요청_결과_2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 2.
        DeliveryResponse 배송지_2 = 배송지_추가_요청_결과_2.as(DeliveryResponse.class);
        ExtractableResponse<Response> 배송지_삭제_결과 = 배송지_삭제_요청(배송지_2);
        assertThat(배송지_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 2.
        ExtractableResponse<Response> 배송지_목록_조회_결과 = 배송지_목록_조회_요청();
        assertThat(배송지_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(배송지_목록_조회_결과.jsonPath().getList("id", Long.class)).hasSize(1);
        assertThat(배송지_목록_조회_결과.jsonPath().getList("receiverPhoneNumber", String.class)).containsExactly(
                "010-1234-1234"
        );
    }

    private ExtractableResponse<Response> 배송지_목록_조회_요청() {
        ExtractableResponse<Response> 배송지_목록_조회_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/delivery")
                .then().log().all()
                .extract();
        return 배송지_목록_조회_결과;
    }

    private ExtractableResponse<Response> 배송지_삭제_요청(DeliveryResponse delivery) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().delete("/delivery/{id}", delivery.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 배송지_추가_요청(String nickName, String receiverName, String receiverPhoneNumber,
                                                          String zipCode, String address, String detailAddress,
                                                          String requestMessage) {
        Map<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("receiverName", receiverName);
        params.put("receiverPhoneNumber", receiverPhoneNumber);
        params.put("zipCode", zipCode);
        params.put("address", address);
        params.put("detailAddress", detailAddress);
        params.put("requestMessage", requestMessage);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/delivery")
                .then().log().all()
                .extract();
    }
}
