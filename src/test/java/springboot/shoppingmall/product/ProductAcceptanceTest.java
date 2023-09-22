package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.*;
import static springboot.shoppingmall.product.ProductQnaAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.presentation.response.ProductQnaResponse;
import springboot.shoppingmall.product.presentation.request.ProductRequest;
import springboot.shoppingmall.product.presentation.response.ProductResponse;

public class ProductAcceptanceTest extends AcceptanceTest {
    CategoryResponse 식품;
    CategoryResponse 육류;
    CategoryResponse 생선;

    @BeforeEach
    public void beforeEach(){
        super.acceptance_beforeEach();

        식품 = 카테고리_등록("식품", null).as(CategoryResponse.class);
        육류 = 카테고리_등록("육류", 식품.getId()).as(CategoryResponse.class);
        생선 = 카테고리_등록("생선", 식품.getId()).as(CategoryResponse.class);
    }

    /**
     * Feature: 상품을 등록한다.
     *  background
     *      given: 카테고리가 미리 등록되어 있음
     *      and : 판매자격이 승인된 판매자 계정이 로그인되어 있음
     *
     *  Scenario: 상품을 등록한다.
     *      given: 상품정보를 입력하고
     *      when: 상품을 등록하면
     *      then: 등록한 상품이 조회된다.
     */
    @Test
    @DisplayName("신규 상품 등록")
    void saveProduct(){
        // given

        // when
        ExtractableResponse<Response> 상품_등록_요청_결과 = 상품_등록_요청_이미지_포함("한돈 돼지고기", 10000, 100 , 식품.getId(), 육류.getId());
        ProductResponse 상품 = 상품_등록_요청_결과.as(ProductResponse.class);

        // then
        ExtractableResponse<Response> 상품_조회_요청_결과 = 상품_조회_요청(상품.getId());
        assertThat(상품_조회_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(상품_조회_요청_결과.jsonPath().getLong("partnerId")).isNotNull();
        assertThat(상품_조회_요청_결과.jsonPath().getString("detail")).isNotNull();
        assertThat(상품_조회_요청_결과.jsonPath().getString("productCode")).isNotNull();
    }

    /**
     *  given: 상품이 기존에 등록되어 있음
     *  And: 상품에 대한 문의와 리뷰가 몇가지 등록되어 있음
     *  when: 상품 정보를 조회하면
     *  then: 상품의 기본 정보와 더불어 리뷰목록도 함께 조회한다.
     */
    @Test
    @DisplayName("상품 정보 조회 - 문의 목록도 조회된다.")
    void find_product_test() {
        // given
        ProductResponse 상품_1번 = 상품_등록_요청("1번 상품", 12000, 1000, 식품.getId(), 육류.getId()).as(ProductResponse.class);
        ProductQnaResponse 문의1 = 문의_등록_요청(상품_1번, "1번 상품 문의입니다.").as(ProductQnaResponse.class);
        ProductQnaResponse 문의2 = 문의_등록_요청(상품_1번, "2번 상품 문의입니다.").as(ProductQnaResponse.class);

        // when
        ExtractableResponse<Response> 상품_정보_조회 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products/{id}", 상품_1번.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(상품_정보_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(상품_정보_조회.jsonPath().getList("qnas.id", Long.class)).containsExactly(
                문의1.getId(), 문의2.getId()
        );
    }

    public static ExtractableResponse<Response> 상품_등록_요청_이미지_포함(String productName, int price, int count, Long categoryId, Long subCategoryId) {
        MultiPartSpecification data = getDataOfMultipart(productName, price, count,
                categoryId, subCategoryId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .multiPart(data)
                .multiPart("file", "test.png", "image content".getBytes(StandardCharsets.UTF_8))
                .when()
                .post("/products")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String productName, int price, int count, Long categoryId, Long subCategoryId) {
        MultiPartSpecification data = getDataOfMultipart(productName, price, count,
                categoryId, subCategoryId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .multiPart(data)
                .multiPart("file", "test.png", "".getBytes(StandardCharsets.UTF_8))
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    private static MultiPartSpecification getDataOfMultipart(String productName, int price, int count,
                                                                    Long categoryId, Long subCategoryId) {
        ProductRequest productRequest = new ProductRequest(
                productName, price, count, categoryId, subCategoryId, "상품 설명 입니다."
        );
        return new MultiPartSpecBuilder(productRequest, ObjectMapperType.JACKSON_2)
                .controlName("data")
                .mimeType(MediaType.APPLICATION_JSON_VALUE)
                .charset("UTF-8")
                .build();
    }

    private ExtractableResponse<Response> 상품_조회_요청(Long productId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/products/{id}", productId)
                .then().log().all()
                .extract();
    }
}
