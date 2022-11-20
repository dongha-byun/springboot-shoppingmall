package springboot.shoppingmall.api.user;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.dto.user.SignUpRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SignUpAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach(){
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = this.port;
        }
    }

    /**
     * given 회원가입 정보를 생성하고
     * when 회원가입을 시도하면
     * then 회원가입에 성공한다.
     */
    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess(){
        // given

        // when
        ExtractableResponse<Response> response = 회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().get("returnCode").toString()).isEqualTo("0");
    }

    /**
     * given 회원가입을 하고
     * when 아이디 찾기를 시도하면
     * then 마스킹된 아이디가 조회된다.
     */
    @Test
    @DisplayName("아이디 조회에 성공한다.")
    void findIdSuccess(){
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        Map<String, String> param = new HashMap<>();
        param.put("name", "변동하");
        param.put("telNo", "010-1234-1234");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().get("/find-id")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().get("body.loginId").toString()).isEqualTo(
                "dongha"
        );
    }

    public static ExtractableResponse<Response> 회원가입(String name, String loginId, String password, String confirmPassword, String telNo) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("loginId", loginId);
        param.put("password", password);
        param.put("confirmPassword", confirmPassword);
        param.put("telNo", telNo);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("sign-up")
                .then().log().all()
                .extract();
        return response;
    }
}
