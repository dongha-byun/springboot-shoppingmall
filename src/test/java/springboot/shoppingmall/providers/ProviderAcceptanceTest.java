package springboot.shoppingmall.providers;

import static org.assertj.core.api.Assertions.*;

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
import springboot.shoppingmall.providers.web.ProviderResponse;

public class ProviderAcceptanceTest extends AcceptanceTest {

    String name = "(주) 부실건설";
    String ceoName = "김아무개";
    String corporateRegistrationNumber = "110-43-22334";
    String telNo = "157-6789";
    String address = "서울시 영등포구 당산동";

    String loginId = "danger_architect";
    String password = "1q2w3e4r!";
    String confirmPassword = "1q2w3e4r!";

    /**
     * given: 판매자 정보를 작성해서
     * when: 판매 자격승인 신청을 하면
     * then: 판매 자격승인이 등록된다.
     */
    @Test
    @DisplayName("판매 자격 신청 성공")
    void create_test() {
        // given

        // when
        ExtractableResponse<Response> 판매_승인요청_등록_결과 = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber,
                telNo, address, loginId, password, confirmPassword);

        // then
        assertThat(판매_승인요청_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ProviderResponse 판매_승인요청_결과 = 판매_승인요청_등록_결과.as(ProviderResponse.class);

        assertThat(판매_승인요청_결과.getId()).isNotNull();
        assertThat(판매_승인요청_결과.isApproved()).isFalse();
    }

    /**
     * given: 판매자 정보에서 회사 이름을 누락하고
     * when: 판매 자격승인 신청을 하면
     * then: 판매 자격승인 신청에 실패한다.
     */
    @Test
    @DisplayName("판매 승인 요청 시, 회사명은 필수입니다.")
    void create_fail_test_no_name() {
        // given

        // when
        ExtractableResponse<Response> 판매_승인요청_등록_결과 = 판매_승인요청_등록_요청("", ceoName, corporateRegistrationNumber,
                telNo, address, loginId, password, confirmPassword);

        // then
        assertThat(판매_승인요청_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(판매_승인요청_등록_결과.jsonPath().getString("message")).contains(
                "사업체 이름은 필수항목 입니다."
        );
    }

    /**
     * given: 판매자 정보에서 사업자 번호를 누락하고
     * when: 판매 자격승인 신청을 하면
     * then: 판매 자격승인 신청에 실패한다.
     */
    @Test
    @DisplayName("판매 승인 요청 시, 사업자 번호는 필수입니다.")
    void create_fail_test_no_corporate_registration_number() {
        // given

        // when
        ExtractableResponse<Response> 판매_승인요청_등록_결과 = 판매_승인요청_등록_요청(name, ceoName, "",
                telNo, address, loginId, password, confirmPassword);

        // then
        assertThat(판매_승인요청_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(판매_승인요청_등록_결과.jsonPath().getString("message")).contains(
                "사업자번호는 필수항목 입니다."
        );
    }

    /**
     * given: 판매자 정보에서 대표번호를 누락하고
     * when: 판매 자격승인 신청을 하면
     * then: 판매 자격승인 신청에 실패한다.
     */
    @Test
    @DisplayName("판매 승인 요청 시, 대표번호는 필수입니다.")
    void create_fail_test_no_tel_no() {
        // given

        // when
        ExtractableResponse<Response> 판매_승인요청_등록_결과 = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber,
                "", address, loginId, password, confirmPassword);

        // then
        assertThat(판매_승인요청_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(판매_승인요청_등록_결과.jsonPath().getString("message")).contains(
                "대표번호는 필수항목 입니다."
        );
    }

    /**
     * given: 판매자 정보에서 주소를 누락하고
     * when: 판매 자격승인 신청을 하면
     * then: 판매 자격승인 신청에 실패한다.
     */
    @Test
    @DisplayName("판매 승인 요청 시, 사업장 주소는 필수입니다.")
    void create_fail_test_no_address() {
        // given

        // when
        ExtractableResponse<Response> 판매_승인요청_등록_결과 = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber,
                telNo, "", loginId, password, confirmPassword);

        // then
        assertThat(판매_승인요청_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(판매_승인요청_등록_결과.jsonPath().getString("message")).contains(
                "사업장 주소는 필수항목 입니다."
        );
    }

    private ExtractableResponse<Response> 판매_승인요청_등록_요청(String name, String ceoName,
                                                        String corporateRegistrationNumber, String telNo,
                                                        String address, String loginId,
                                                        String password, String confirmPassword) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("ceoName", ceoName);
        params.put("corporateRegistrationNumber", corporateRegistrationNumber);
        params.put("telNo", telNo);
        params.put("address", address);
        params.put("loginId", loginId);
        params.put("password", password);
        params.put("confirmPassword", confirmPassword);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/providers")
                .then().log().all()
                .extract();
    }
}
