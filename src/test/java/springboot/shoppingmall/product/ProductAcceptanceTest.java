package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;

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
