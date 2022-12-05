package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.*;

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
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.dto.ProductRequest;

public class ProductAcceptanceTest extends AcceptanceTest {

    CategoryResponse 식품;
    CategoryResponse 육류;
    CategoryResponse 생선;

    @BeforeEach
    public void beforeEach(){
        super.beforeEach();

        식품 = 카테고리_등록("식품", null).as(CategoryResponse.class);
        육류 = 카테고리_등록("육류", 식품.getId()).as(CategoryResponse.class);
        생선 = 카테고리_등록("생선", 식품.getId()).as(CategoryResponse.class);
    }

    /**
     * Feature: 상품을 등록한다.
     *  background
     *      given: 카테고리가 미리 등록되어 있음
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
        ProductRequest productRequest = new ProductRequest("한돈 돼지고기", 10000, 100, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 상품_등록_요청_결과 = 상품_등록_요청(productRequest);

        // then
        ExtractableResponse<Response> 상품_조회_요청_결과 = 상품_조회_요청(상품_등록_요청_결과);
        assertThat(상품_조회_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 상품_등록_요청(ProductRequest productRequest) {
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("x-auth-token", "testToken");

        Map<String, Object> params = new HashMap<>();
        params.put("name", productRequest.getName());
        params.put("price", productRequest.getPrice());
        params.put("count", productRequest.getCount());
        params.put("categoryId", productRequest.getCategoryId());
        params.put("subCategoryId", productRequest.getSubCategoryId());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headerParam)
                .body(params)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_조회_요청(ExtractableResponse<Response> response) {
        CategoryResponse categoryResponse = response.as(CategoryResponse.class);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products/{id}", categoryResponse.getId())
                .then().log().all()
                .extract();
    }
}
