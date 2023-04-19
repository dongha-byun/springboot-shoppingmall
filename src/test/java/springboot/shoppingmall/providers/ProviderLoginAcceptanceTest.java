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
    /**
     *  given: 판매 자격 신청이 승인된 판매자 계정이 존재함.
     *  when: 판매 자격 신청이 완료된 계정으로 로그인을 시도하면
     *  then: 판매자 로그인에 성공한다.
     */
    @Test
    @DisplayName("자격 승인이 완료된 판매자는 로그인에 성공한다.")
    void provider_login_success_with_complete_authentication() {
        // given

        // when
        ExtractableResponse<Response> 판매자_로그인_결과 = 판매자_로그인_요청(loginId, password);

        // then
        assertThat(판매자_로그인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매자_로그인_결과.jsonPath().getString("accessToken")).isNotNull();
    }

    public static ExtractableResponse<Response> 판매자_로그인_요청(String loginId, String password) {
        Map<String, String> param = new HashMap<>();
        param.put("loginId", loginId);
        param.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().get("/providers/login")
                .then().log().all()
                .extract();
    }
}
