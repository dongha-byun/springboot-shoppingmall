package springboot.shoppingmall.authorization;

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
import springboot.shoppingmall.authorization.dto.TokenResponse;

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
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    /**
     * given 회원가입을 하고
     * when 잘못된 비밀번호로 로그인을 시도하면
     * then 로그인에 실패하고, 로그인 실패 횟수가 증가한다.
     */
    @Test
    @DisplayName("비밀번호를 잘못 입력하면, 로그인 실패 횟수가 증가한다.")
    void increase_login_fail_count_by_wrong_password() {
        // given
        회원가입("변동하", "user1", "user1!", "user1!", "010-1111-2222");

        // when
        ExtractableResponse<Response> 로그인_결과 = 로그인("user1", "user2!");

        // then
        assertThat(로그인_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(로그인_결과.jsonPath().getString("message")).contains("1");
    }

    /**
     * given 회원가입을 하고
     * when 잘못된 비밀번호로 로그인을 시도하면
     * then 로그인에 실패하고, 로그인 실패 횟수가 증가한다.
     */
    @Test
    @DisplayName("로그인 실패횟수가 5회 이상인 경우, 해당 계정으로 로그인할 수 없다.")
    void login_fail_by_fail_count_over_5() {
        // given
        회원가입("변동하", "user1", "user1!", "user1!", "010-1111-2222");

        // when
        로그인("user1", "user2!");
        로그인("user1", "user2!");
        로그인("user1", "user2!");
        로그인("user1", "user2!");
        로그인("user1", "user2!");
        ExtractableResponse<Response> 로그인_5번째_결과 = 로그인("user1", "user2!");

        // then
        assertThat(로그인_5번째_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(로그인_5번째_결과.jsonPath().getString("message")).isEqualTo(
                "해당 계정은 로그인 실패 횟수 5회를 초과하여 로그인 할 수 없습니다. 관리자에게 문의해주세요."
        );
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
