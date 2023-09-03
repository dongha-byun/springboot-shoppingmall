package springboot.shoppingmall.userservice.authentication.email;

import static org.assertj.core.api.Assertions.assertThat;

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

public class AuthenticationEmailAcceptanceTest extends AcceptanceTest {

    /**
     * given 이메일을 입력하고, 인증번호를 발급받는다.
     * when 입력한 인증번호가 발급받은 인증번호와 일치하면
     * then 인증에 성공한다.
     */
    @Test
    @DisplayName("회원가입 시, 이메일로 발급받은 인증번호를 입력하면 회원가입을 할 수 있게 된다.")
    void email_auth_check() {
        // given
        ExtractableResponse<Response> 인증번호_발급_결과 = 인증번호_발급_요청("authTest@test.com");
        assertThat(인증번호_발급_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(인증번호_발급_결과.jsonPath().getString("email")).isEqualTo("authTest@test.com");
        assertThat(인증번호_발급_결과.jsonPath().getString("expireTime")).isNotNull();

        // when
        ExtractableResponse<Response> 인증번호_확인_결과 = 인증번호_확인하기_요청("authTest@test.com", "012345");

        // then
        assertThat(인증번호_확인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(인증번호_확인_결과.jsonPath().getString("email")).isEqualTo("authTest@test.com");
    }

    /**
     * given: 이메일로 인증번호를 발급받고,
     * when: 발급받은 인증번호와 다른 인증번호를 입력하면
     * then: 인증번호가 틀리다는 메세지와 함께 회원가입 절차로 넘어가지 못한다.
     */
    @Test
    @DisplayName("발급된 인증번호와 다른 인증번호를 입력하면, 이메일 인증에 실패한다.")
    void wrong_auth_code() {
        // given
        ExtractableResponse<Response> 인증번호_발급_결과 = 인증번호_발급_요청("test@test.com");
        assertThat(인증번호_발급_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> 인증번호_확인하기_결과 = 인증번호_확인하기_요청("test@test.com", "543210");

        // then
        assertThat(인증번호_확인하기_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(인증번호_확인하기_결과.jsonPath().getString("message")).isEqualTo(
                "인증번호가 맞지 않습니다."
        );
    }

    private ExtractableResponse<Response> 인증번호_확인하기_요청(String email, String code) {
        Map<String, String> param2 = new HashMap<>();
        param2.put("email", email);
        param2.put("code", code);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param2)
                .when().post("/check-authorized-code") // 이메일로 발송한 인증번호가 맞는지 확인하기 위한 REST API
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 인증번호_발급_요청(String email) {
        Map<String, String> param1 = new HashMap<>();
        param1.put("email", email);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param1)
                .when().post("/send-authorize-code") // 이메일로 인증번호 발송을 위한 REST API
                .then().log().all()
                .extract();
    }
}
