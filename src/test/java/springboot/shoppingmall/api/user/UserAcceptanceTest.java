package springboot.shoppingmall.api.user;

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

public class UserAcceptanceTest extends AcceptanceTest {

    /**
     * given 회원가입 정보를 생성하고 when 회원가입을 시도하면 then 회원가입에 성공한다.
     */
    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess() {
        // given

        // when
        ExtractableResponse<Response> response = 회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given 회원가입을 하고 when 아이디 찾기를 시도하면 then 마스킹된 아이디가 조회된다.
     */
    @Test
    @DisplayName("아이디 조회에 성공한다.")
    void findIdSuccess() {
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        ExtractableResponse<Response> response = 아이디_찾기("변동하", "010-1234-1234");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().get("loginId").toString()).isEqualTo(
                "dongha"
        );
    }

    /**
     * given 회원가입을 하고 when 비밀번호 찾기를 시도하면 then 마스킹된 아이디가 조회된다.
     */
    @Test
    @DisplayName("회원정보 조회에 성공한다.")
    void findPwSuccess() {
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        ExtractableResponse<Response> response = 회원정보_조회("변동하", "010-1234-1234","dongha");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().get("name").toString()).isEqualTo(
                "변동하"
        );
    }

    private ExtractableResponse<Response> 회원정보_조회(String name, String telNo, String loginId) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("telNo", telNo);
        param.put("loginId", loginId);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().get("/find-pw")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원가입(String name, String loginId, String password,
                                                     String confirmPassword, String telNo) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("loginId", loginId);
        param.put("password", password);
        param.put("confirmPassword", confirmPassword);
        param.put("telNo", telNo);

        // when
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/sign-up")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 아이디_찾기(String name, String telNo) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("telNo", telNo);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().get("/find-id")
                .then().log().all()
                .extract();
    }
}
