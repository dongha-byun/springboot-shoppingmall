package springboot.shoppingmall.providers;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.admin.AdminApproveProviderAcceptanceTest.*;
import static springboot.shoppingmall.providers.ProviderAcceptanceTest.*;

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

public class ProviderLoginAcceptanceTest extends AcceptanceTest {

    String name = "(주) 부실건설";
    String ceoName = "김아무개";
    String corporateRegistrationNumber = "110-43-22334";
    String telNo = "157-6789";
    String address = "서울시 영등포구 당산동";
    String loginId = "danger_architect";
    String password = "1q2w3e4r!";
    String confirmPassword = "1q2w3e4r!";

    /**
     *  given: 판매 자격 신청이 승인된 판매자 계정이 존재함.
     *  when: 판매 자격 신청이 완료된 계정으로 로그인을 시도하면
     *  then: 판매자 로그인에 성공한다.
     */
    @Test
    @DisplayName("자격 승인이 완료된 판매자는 로그인에 성공한다.")
    void provider_login_success_with_complete_authentication() {
        // given
        Long providerId = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber, telNo, address, loginId, password,
                confirmPassword)
                .jsonPath()
                .getLong("data.id");
        ExtractableResponse<Response> 판매_승인_요청_결과 = 판매_승인_요청(providerId);
        assertThat(판매_승인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        Map<String, String> param = new HashMap<>();
        param.put("loginId", loginId);
        param.put("password", password);
        ExtractableResponse<Response> 판매자_로그인_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().get("/providers/login")
                .then().log().all()
                .extract();

        // then
        assertThat(판매자_로그인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
