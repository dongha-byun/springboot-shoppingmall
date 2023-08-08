package springboot.shoppingmall;

import static org.assertj.core.api.Assertions.assertThat;
import static springboot.shoppingmall.admin.AdminApproveProviderAcceptanceTest.판매_승인_요청;
import static springboot.shoppingmall.authorization.LoginAcceptanceTest.로그인;
import static springboot.shoppingmall.providers.ProviderAcceptanceTest.판매_승인요청_등록_요청;
import static springboot.shoppingmall.providers.PartnersLoginAcceptanceTest.판매자_로그인_요청;
import static springboot.shoppingmall.user.UserAcceptanceTest.회원가입;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.db.DatabaseCleanUtil;
import springboot.shoppingmall.message.MessageProvider;
import springboot.shoppingmall.product.configuration.TestFileConfiguration;
import springboot.shoppingmall.providers.web.ProviderTokenResponse;
import springboot.shoppingmall.user.dto.UserResponse;

@Import({
        TestFileConfiguration.class,
        TestOrderConfig.class
})
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

    public static String loginId = "danger_architect";
    public static String password = "1q2w3e4r!";
    public static String 판매자_로그인토큰;

    protected Long partnerId;

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

        partnerId = 판매_승인요청_등록_요청("(주) 부실건설", "김아무개", "110-43-22334", "1577-6789"
                , "서울시 영등포구 당산동", loginId, password, password)
                .jsonPath().getLong("data.id");
        ExtractableResponse<Response> 판매_승인_요청_결과 = 판매_승인_요청(partnerId);
        assertThat(판매_승인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        판매자_로그인토큰 = 판매자_로그인_요청(loginId, password).as(ProviderTokenResponse.class).getAccessToken();
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

    public static Map<String, String> createAuthorizationHeader(String accessToken){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        return headers;
    }

    protected <T> void 목록_조회_결과_검증(ExtractableResponse<Response> response, String path, Class<T> type, T... results){
        assertThat(response.jsonPath().getList(path, type)).containsExactly(
                results
        );
    }
}
