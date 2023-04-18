package springboot.shoppingmall.admin;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.providers.ProviderAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import springboot.shoppingmall.AcceptanceTest;

public class AdminApproveProviderAcceptanceTest extends AcceptanceTest {

    String name = "(주) 부실건설";
    String ceoName = "김아무개";
    String corporateRegistrationNumber = "110-43-22334";
    String telNo = "157-6789";
    String address = "서울시 영등포구 당산동";
    String loginId = "danger_architect";
    String password = "1q2w3e4r!";
    String confirmPassword = "1q2w3e4r!";

    /**
     *  given: 판매자가 회원가입을 통해 자격 신청을 해놓음
     *  when: 로그인한 관리자가 판매자의 자격을 승인하면
     *  then: 판매자는 상품을 판매할 수 있게된다.
     */
    @Test
    @DisplayName("관리자는 판매자가 신청한 판매 자격을 승인할 수 있다.")
    void approve_provider_test() {
        // given
        Long providerId = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber, telNo, address,
                loginId, password, confirmPassword)
                .jsonPath()
                .getLong("data.id");

        // when
        ExtractableResponse<Response> 판매_승인_결과 = 판매_승인_요청(providerId);

        // then
        assertThat(판매_승인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매_승인_결과.jsonPath().getString("message")).contains(
                "판매자격 승인이 완료되었습니다."
        );
        assertThat(판매_승인_결과.jsonPath().getLong("data.id")).isEqualTo(providerId);
        assertThat(판매_승인_결과.jsonPath().getBoolean("data.approved")).isTrue();
    }

    /**
     *  given: 판매자가 회원가입을 통해 자격 신청을 해놓음
     *  And: 판매자의 판매 자격이 승인되어 있음
     *  when: 로그인한 관리자가 판매자의 자격을 정지시키면
     *  then: 판매자는 상품을 판매할 수 없게된다.
     */
    @Test
    @DisplayName("관리자는 판매자의 판매자격을 중지시킬 수 있다.")
    void stop_provider_test() {
        // given
        Long providerId = 판매_승인요청_등록_요청(name, ceoName, corporateRegistrationNumber, telNo, address,
                loginId, password, confirmPassword)
                .jsonPath()
                .getLong("data.id");
        ExtractableResponse<Response> 판매_승인_결과 = 판매_승인_요청(providerId);

        // when
        ExtractableResponse<Response> 판매_자격정지_결과 = RestAssured.given().log().all()
                .when().put("/admin/provider/{providerId}/stop", providerId)
                .then().log().all()
                .extract();

        // then
        assertThat(판매_자격정지_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매_자격정지_결과.jsonPath().getString("message")).contains(
                "해당 판매자의 판매 자격이 중지되었습니다."
        );
        assertThat(판매_자격정지_결과.jsonPath().getLong("data.id")).isEqualTo(providerId);
        assertThat(판매_자격정지_결과.jsonPath().getBoolean("data.approved")).isFalse();
    }

    /**
     *  given: 판매자격 승인 요청을 한 판매업체가 다수 존재함
     *  when: 판매자격 승인 요청을 한 판매업체의 목록을 조회하면
     *  then: 판매자격 승인을 요쳥한 판매업체들의 목록이 모두 조회된다.
     */
    @Test
    @DisplayName("판매자격 승인 요청을 한 판매업체 목록을 조회한다.")
    void find_all_partners() {
        // given
        for(int i=1; i<=5; i++) {
            판매_승인요청_등록_요청("판매업체"+i, "대표님"+i, "110-22-33221"+i
                    , "02-3432-123"+i, "서울시 영등포구"
                    , "partner"+i, "partner1!", "partner1!");
        }

        // then
        ExtractableResponse<Response> 판매자격_승인_요청_목록_요청_결과 = RestAssured.given().log().all()
                .when().get("/admin/partners")
                .then().log().all()
                .extract();

        // when
        assertThat(판매자격_승인_요청_목록_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매자격_승인_요청_목록_요청_결과.jsonPath().getList("name")).containsExactly(
                "판매업체1", "판매업체2", "판매업체3", "판매업체4", "판매업체5"
        );
        assertThat(판매자격_승인_요청_목록_요청_결과.jsonPath().getList("ceoName")).containsExactly(
                "대표님1", "대표님2", "대표님3", "대표님4", "대표님5"
        );
        assertThat(판매자격_승인_요청_목록_요청_결과.jsonPath().getList("telNo")).containsExactly(
                "02-3432-1231", "02-3432-1232", "02-3432-1233", "02-3432-1234", "02-3432-1235"
        );
        assertThat(판매자격_승인_요청_목록_요청_결과.jsonPath().getList("corporateRegistrationNumber")).containsExactly(
                "110-22-332211", "110-22-332212", "110-22-332213", "110-22-332214", "110-22-332215"
        );
    }

    public static ExtractableResponse<Response> 판매_승인_요청(Long providerId) {
        return RestAssured.given().log().all()
                .when().put("/admin/provider/{providerId}/approve", providerId)
                .then().log().all()
                .extract();
    }
}
