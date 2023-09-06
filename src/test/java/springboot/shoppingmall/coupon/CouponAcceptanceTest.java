package springboot.shoppingmall.coupon;

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
import springboot.shoppingmall.coupon.presentation.CouponResponse;
import springboot.shoppingmall.userservice.user.domain.UserGrade;

public class CouponAcceptanceTest extends AcceptanceTest {

    /**
     *  BackGround:
     *      given: 판매자가 로그인이 되어 있음
     *      And: 사용자 몇 명이 회원가입 되어 있음
     *  Scenario:
     *      when: 판매자가 쿠폰정보를 입력하고 쿠폰을 등록하면
     *      then: 대상 사용자들 앞으로 사용이 가능한 쿠폰이 발행된다.
     */
    @DisplayName("판매자가 고객에게 쿠폰을 발급한다.")
    @Test
    void create_coupon_by_partners() {
        // given
        // 1. 판매자 로그인 토큰 발행 완료 - AcceptanceTest
        // 2. 인수테스터1, 인수테스터2 회원가입 완료 - AcceptanceTest

        // when
        Map<String, Object> params = new HashMap<>();
        params.put("name", "할인쿠폰#1");
        params.put("fromDate", "2023-07-01");
        params.put("toDate", "2023-12-31");
        params.put("userGrade", UserGrade.NORMAL.name());
        params.put("discountRate", 5);

        ExtractableResponse<Response> 쿠폰_생성_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .body(params)
                .when().post("/coupons")
                .then().log().all()
                .extract();

        assertThat(쿠폰_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        CouponResponse 쿠폰 = 쿠폰_생성_결과.as(CouponResponse.class);
        assertThat(쿠폰.getCouponId()).isNotNull();
        assertThat(쿠폰.getMessage()).isEqualTo(
                "쿠폰이 정상적으로 등록되었습니다."
        );

        // then
        ExtractableResponse<Response> 쿠폰_발급_대상자_조회 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/coupons/{id}/users", 쿠폰.getCouponId())
                .then().log().all()
                .extract();
        assertThat(쿠폰_발급_대상자_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(쿠폰_발급_대상자_조회.jsonPath().getList("userId", Long.class)).containsExactly(
                인수테스터1.getId(), 인수테스터2.getId()
        );
    }
}
