package springboot.shoppingmall.user;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.user.UserAcceptanceTest.회원가입;

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

public class LoginAcceptanceTest extends AcceptanceTest {

    /**
     * given 회원가입을 하고 when 로그인을 시도하면 then 로그인이 성공한다.
     */
    @Test
    @DisplayName("회원가입 후, 로그인을 시도한다.")
    void loginAfterSignUp() {
        // given
        회원가입("변동하", "user1", "user1!", "user1!", "010-1111-2222");

        // when
        ExtractableResponse<Response> loginResponse = 로그인("user1", "user1!");

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginResponse.jsonPath().get("returnCode").toString()).isEqualTo("0");
    }

    public static ExtractableResponse<Response> 로그인(String loginId, String password) {
        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("loginId", loginId);
        loginParam.put("password", password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
