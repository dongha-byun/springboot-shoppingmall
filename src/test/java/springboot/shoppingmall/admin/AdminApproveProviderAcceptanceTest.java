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
        ExtractableResponse<Response> 판매_승인_결과 = RestAssured.given().log().all()
                .when().put("/admin/provider/{providerId}/approve", providerId)
                .then().log().all()
                .extract();

        // then
        assertThat(판매_승인_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매_승인_결과.jsonPath().getString("message")).contains(
                "판매자격 승인이 완료되었습니다."
        );
        assertThat(판매_승인_결과.jsonPath().getLong("data.id")).isEqualTo(providerId);
        assertThat(판매_승인_결과.jsonPath().getBoolean("data.approved")).isTrue();

    }
}
