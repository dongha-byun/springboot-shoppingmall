package springboot.shoppingmall.user;

import static org.assertj.core.api.Assertions.assertThat;
import static springboot.shoppingmall.authorization.LoginAcceptanceTest.*;

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
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.dto.UserResponse;

public class UserAcceptanceTest extends AcceptanceTest {

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
        assertThat(인증번호_발급_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> 인증번호_확인_결과 = 인증번호_확인하기_요청("authTest@test.com", "012345");

        // then
        assertThat(인증번호_확인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        assertThat(인증번호_발급_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

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

    /**
     * given 회원가입 정보를 생성하고
     * when 회원가입을 시도하면
     * then 회원가입에 성공한다.
     */
    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess() {
        // given

        // when
        ExtractableResponse<Response> 회원가입_결과 = 회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // then
        assertThat(회원가입_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        UserResponse 회원가입_정보 = 회원가입_결과.as(UserResponse.class);
        assertThat(회원가입_정보.getId()).isNotNull();
        assertThat(회원가입_정보.getName()).isEqualTo("변동하");
        assertThat(회원가입_정보.getLoginId()).isEqualTo("dongha");
        assertThat(회원가입_정보.getTelNo()).isEqualTo("010-1234-1234");
    }

    /**
     * given 회원가입을 하고
     * when 아이디 찾기를 시도하면
     * then 마스킹된 아이디가 조회된다.
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
                "do****"
        );
    }

    /**
     * given 회원가입을 하고
     * when 비밀번호 찾기를 시도하면
     * then 마스킹된 아이디가 조회된다.
     */
    @Test
    @DisplayName("회원정보 조회에 성공한다.")
    void findPwSuccess() {
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        ExtractableResponse<Response> response = 비밀번호_찾기("변동하", "010-1234-1234","dongha");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    /**
     * given 회원가입한 회원으로 로그인하고
     * when password 와 연락처를 변경하면
     * then 회원정보 조회 시, 변경된 정보가 조회된다.
     */
    @Test
    @DisplayName("회원정보 수정에 성공한다.")
    void updateUser() {
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");
        TokenResponse login = 로그인("dongha", "dongha1!").as(TokenResponse.class);

        // when
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + login.getAccessToken());

        Map<String, String> param = new HashMap<>();
        param.put("password", "dongha2@");
        param.put("telNo", "010-4567-4567");
        RestAssured.given().log().all()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().put("/user")
                .then().log().all()
                .extract();

        // then
        UserResponse response = 회원정보_조회(login.getAccessToken()).as(UserResponse.class);

        assertThat(response.getTelNo()).isEqualTo("010-4567-4567");
    }

    /**
     * given: 회원가입한 회원으로 로그인하고
     * when: 내 정보를 조회하면
     * then: 현재 회원등급과 다음 회원등급, 다음 회원 등급까지 남은 조건이 조회된다.
     */
    @Test
    @DisplayName("현재 회원등급 / 다음 회원등급이 조회된다.")
    void viewing_user_grade_info() {
        // given
        회원가입("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");
        TokenResponse 사용자_로그인 = 로그인("dongha", "dongha1!").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 회원등급_조회_결과 = 회원등급_조회(사용자_로그인);

        // then
        assertThat(회원등급_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(회원등급_조회_결과.jsonPath().getString("userName")).isEqualTo("변동하");
        assertThat(회원등급_조회_결과.jsonPath().getString("currentUserGrade")).isEqualTo("일반회원");
        assertThat(회원등급_조회_결과.jsonPath().getString("nextUserGrade")).isEqualTo("단골회원");
        assertThat(회원등급_조회_결과.jsonPath().getInt("remainedOrderCountForNextGrade")).isEqualTo(10);
        assertThat(회원등급_조회_결과.jsonPath().getInt("remainedAmountsForNextGrade")).isEqualTo(50000);
        assertThat(회원등급_조회_결과.jsonPath().getInt("gradeDiscountRate")).isEqualTo(UserGrade.NORMAL.getDiscountRate());
    }

    private ExtractableResponse<Response> 회원등급_조회(TokenResponse login) {
        return RestAssured.given().log().all()
                .headers(createAuthorizationHeader(login))
                .when().get("/user/grade-info")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원정보_조회(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return RestAssured.given().log().all()
                .headers(headers)
                .when().get("/user")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 비밀번호_찾기(String name, String telNo, String loginId) {
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
