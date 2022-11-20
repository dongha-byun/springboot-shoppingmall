package springboot.shoppingmall.api;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.api.user.SignUpAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.api.user.SignUpAcceptanceTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach(){
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = port;
        }
    }

    /**
     * given 회원가입을 하고
     * when 로그인을 시도하면
     * then 로그인이 성공한다.
     */
    @Test
    @DisplayName("회원가입 후, 로그인을 시도한다.")
    void loginAfterSignUp(){
        // given
        ExtractableResponse<Response> response = 회원가입("변동하", "user1", "user1!", "user1!", "010-1111-2222");

        // when
        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("loginId", "user1");
        loginParam.put("password", "user1!");
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .extract();

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginResponse.jsonPath().get("returnCode").toString()).isEqualTo("0");
    }
}
