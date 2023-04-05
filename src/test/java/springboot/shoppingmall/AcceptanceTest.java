package springboot.shoppingmall;

import static springboot.shoppingmall.authorization.LoginAcceptanceTest.로그인;
import static springboot.shoppingmall.user.UserAcceptanceTest.회원가입;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.db.DatabaseCleanUtil;
import springboot.shoppingmall.message.MessageProvider;
import springboot.shoppingmall.user.dto.UserResponse;

@Import(TestOrderConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    public MessageProvider messageProvider;
    @Autowired
    DatabaseCleanUtil databaseCleanUtil;

    String LOGIN_ID_1 = "acceptanceTester";
    String PASSWORD_1 = "test1!";

    String LOGIN_ID_2 = "acceptanceTester2";
    String PASSWORD_2 = "test2@";

    public UserResponse 인수테스터1;
    public UserResponse 인수테스터2;

    public static TokenResponse 로그인정보;
    public static TokenResponse 로그인정보2;

    @BeforeEach
    public void acceptance_beforeEach(){
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = this.port;
        }
        databaseCleanUtil.cleanUp();

        인수테스터1 = 회원가입("인수테스터1", LOGIN_ID_1, PASSWORD_1, PASSWORD_1, "010-1234-1234").as(UserResponse.class);
        로그인정보 = 로그인(LOGIN_ID_1, PASSWORD_1).as(TokenResponse.class);

        인수테스터2 = 회원가입("인수테스터2", LOGIN_ID_2, PASSWORD_2, PASSWORD_2, "010-1111-4444").as(UserResponse.class);
        로그인정보2 = 로그인(LOGIN_ID_2, PASSWORD_2).as(TokenResponse.class);
    }

    @AfterEach
    public void acceptance_afterEach() {
        databaseCleanUtil.cleanUp();
    }

    public static Map<String, String> createAuthorizationHeader(TokenResponse tokenResponse){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + tokenResponse.getAccessToken());

        return headers;
    }
}
