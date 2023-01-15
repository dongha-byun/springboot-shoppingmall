package springboot.shoppingmall.product;

import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;
import static springboot.shoppingmall.product.ProductQnaAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.dto.ProductQnaResponse;
import springboot.shoppingmall.product.dto.ProductResponse;

public class ProductQnaAnswerAcceptanceTest extends AcceptanceTest {

    ProductResponse 상품;
    ProductQnaResponse 상품_문의;

    @BeforeEach
    void setUp(){
        super.beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        상품_문의 = 문의_등록_요청(상품, "문의 남깁니다. 답변 부탁드려요 ㅠ").as(ProductQnaResponse.class);
    }

    /**
     * Feature: 문의 답변 등록
     *  Background:
     *      given: 판매자 계정 로그인 되어있음
     *      And: 등록된 문의글이 있음
     *  Scenario: 문의 답변 등록
     *      when: 문의에 대한 답변을 작성하면
     *      then: 답변이 작성됐다는 메일을 문의 작성자에게 보낸다.
     *      then: 문의 조회 시, 답변이 같이 조회된다.
     */
    @Test
    @DisplayName("문의 답변을 등록한다.")
    void answerCreateTest(){
        // given

        // when
        ExtractableResponse<Response> 문의_답변_작성_결과 = 문의_답변_등록_요청(상품, 상품_문의, "답변드립니다. 불편을 드려 죄송합니다. 감사합니다.");

        // then
        Assertions.assertThat(문의_답변_작성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 문의_답변_등록_요청(ProductResponse productResponse, ProductQnaResponse qnaResponse, String content) {
        Map<String, String> body = new HashMap<>();
        body.put("content", content);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(body)
                .when().post("/products/{productId}/qna/{qnaId}/answer", productResponse.getId(), qnaResponse.getId())
                .then().log().all()
                .extract();
    }
}
