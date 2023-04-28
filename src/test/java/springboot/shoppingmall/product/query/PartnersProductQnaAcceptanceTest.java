package springboot.shoppingmall.product.query;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.product.ProductQnaAcceptanceTest.*;
import static springboot.shoppingmall.product.ProductQnaAnswerAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceProductTest;
import springboot.shoppingmall.product.dto.ProductQnaAnswerResponse;
import springboot.shoppingmall.product.dto.ProductQnaResponse;

public class PartnersProductQnaAcceptanceTest extends AcceptanceProductTest {

    /**
     *  given: 판매자가 상품을 등록해놓음
     *  And: 판매자가 등록한 상품에 문의가 등록되어있음
     *  when: 판매자가 등록된 문의 목록을 조회하면,
     *  then: 답변 등록이 안 된 문의들이 목록에 조회된다.
     */
    @Test
    @DisplayName("판매자가 등록한 상품에 등록된 문의 중 답변이 완료되지 않은 문의들을 조회한다.")
    void partners_product_qna_no_answer() {
        // given
        ProductQnaResponse 문의1 = 문의_등록_요청(상품, "상품 문의 드립니다.").as(ProductQnaResponse.class);
        ProductQnaResponse 문의2 = 문의_등록_요청(상품2, "상품2 문의 드립니다.").as(ProductQnaResponse.class);

        // when
        ExtractableResponse<Response> 문의_목록_조회_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/product/qnas?isAnswered=N")
                .then().log().all()
                .extract();

        // then
        assertThat(문의_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.id")).containsExactly(
                문의1.getId().intValue(), 문의2.getId().intValue()
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.content")).containsExactly(
                문의1.getContent(), 문의2.getContent()
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.writerName")).containsExactly(
                "인수테스터1", "인수테스터1"
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.answered")).containsExactly(
                false, false
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.productName")).containsExactly(
                상품.getName(), 상품2.getName()
        );
    }

    /**
     *  given: 판매자가 상품을 등록해놓음
     *  And: 판매자가 등록한 상품에 문의가 등록되어있음
     *  when: 판매자가 등록된 문의 목록을 조회하면,
     *  then: 답변이 등록된 문의들이 목록에 조회된다.
     */
    @Test
    @DisplayName("판매자가 등록한 상품에 등록된 문의 중 답변이 완료된 문의들을 조회한다.")
    void partners_product_qna_has_answer() {
        // given
        ProductQnaResponse 문의1 = 문의_등록_요청(상품, "상품 문의 드립니다.").as(ProductQnaResponse.class);
        ProductQnaResponse 문의2 = 문의_등록_요청(상품2, "상품2 문의 드립니다.").as(ProductQnaResponse.class);
        ProductQnaAnswerResponse 문의2_답변 = 문의_답변_등록_요청(상품2, 문의2, "문의에 답변 드립니다.").as(ProductQnaAnswerResponse.class);

        // when
        ExtractableResponse<Response> 문의_목록_조회_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/product/qnas?isAnswered=Y")
                .then().log().all()
                .extract();

        // then
        assertThat(문의_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.id")).containsExactly(
                문의2.getId().intValue()
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.content")).containsExactly(
                문의2.getContent()
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.writerName")).containsExactly(
                "인수테스터1"
        );
        assertThat(문의_목록_조회_결과.jsonPath().getList("data.answered")).containsExactly(
                true
        );
    }
}
