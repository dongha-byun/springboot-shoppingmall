package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.*;
import static springboot.shoppingmall.product.ProductAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.dto.ProductQnaResponse;
import springboot.shoppingmall.product.dto.ProductResponse;

public class ProductQnaAcceptanceTest extends AcceptanceTest {
    ProductResponse 상품;
    TokenResponse 로그인정보;

    @BeforeEach
    void setUp(){
        super.beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);

    }
    /**
     * given 로그인한 사용자와 상품이 등록되어 있고
     * when 사용자가 상품에 문의글을 올리면
     * then 상품 문의가 등록된다.
     */
    @Test
    @DisplayName("상품 문의를 등록한다.")
    void writeQnaTest(){
        // given

        // when
        ExtractableResponse<Response> 문의_등록_요청_결과 = 문의_등록_요청(상품, "상품 문의 등록합니다. 이상해요 제품이... 확인 좀 해주세요 ㅠㅠㅠ");

        // then
        assertThat(문의_등록_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     *  given 상품문의를 등록하고
     *  when 해당 상품의 문의내역을 조회하면
     *  then 문의 목록이 조회된다.
     */
    @Test
    @DisplayName("상품 문의 목록을 조회한다.")
    void findAllQnaTest(){
        // given
        ProductQnaResponse 문의_1 = 문의_등록_요청(상품, "상품 문의 등록합니다 1.").as(ProductQnaResponse.class);
        ProductQnaResponse 문의_2 = 문의_등록_요청(상품, "상품 문의 등록합니다 2.").as(ProductQnaResponse.class);

        // when
        ExtractableResponse<Response> response = 문의_목록_조회_요청(상품);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 문의_목록_조회_요청(ProductResponse productResponse) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products/{id}/qna", productResponse.getId())
                .then().log().all()
                .extract();
        return response;
    }

    private ExtractableResponse<Response> 문의_등록_요청(ProductResponse productResponse, String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("content", content);

        return RestAssured.given().log().all()
                .headers(createAuthorizationHeader(로그인정보))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/products/{id}/qna", productResponse.getId())
                .then().log().all()
                .extract();
    }
}
