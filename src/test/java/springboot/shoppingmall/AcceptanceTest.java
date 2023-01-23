package springboot.shoppingmall;

import static springboot.shoppingmall.authorization.LoginAcceptanceTest.로그인;
import static springboot.shoppingmall.user.UserAcceptanceTest.회원가입;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.db.DatabaseCleanUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUtil databaseCleanUtil;

    String LOGIN_ID = "acceptanceTester";
    String PASSWORD = "test1!";
    public static TokenResponse 로그인정보;

    @BeforeEach
    public void acceptance_beforeEach(){
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = this.port;
        }
        databaseCleanUtil.cleanUp();

        회원가입("인수테스터1", LOGIN_ID, PASSWORD, PASSWORD, "010-1234-1234");
        로그인정보 = 로그인(LOGIN_ID, PASSWORD).as(TokenResponse.class);
    }

    public static Map<String, String> createAuthorizationHeader(TokenResponse tokenResponse){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + tokenResponse.getAccessToken());

        return headers;
    }
}
