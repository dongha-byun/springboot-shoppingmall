package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.product.ProductAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

public class ProductQueryAcceptanceTest extends AcceptanceTest {

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
     * Feature: 상품목록 정렬 테스트
     *  Background:
     *      given: 하나의 카테고리에 여러 개의 등록된 상품들이 있음
     *  Scenario: 상품 목록 정렬
     *      when: "평점 높은 순" 으로 목록을 조회하면
     *      then: 평점이 높은 순 부터 낮은 순으로 상품이 조회된다.
     */
    @Test
    @DisplayName("상품 정렬 테스트 - 평점 높은 순")
    void sort_products_by_score() {
        // given
        상품_등록_요청("육류1", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류2", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류3", 15000, 20, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 높은_평점순_목록_조회_결과 = 상품_목록_조회_요청(식품, 육류, ProductQueryOrderType.SCORE);

        // then
        assertThat(높은_평점순_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(높은_평점순_목록_조회_결과.jsonPath().getList("name")).containsExactly(
                "육류2", "육류1", "육류3"
        );
    }

    /**
     * Feature: 상품목록 정렬 테스트
     *  Background:
     *      given: 하나의 카테고리에 여러 개의 등록된 상품들이 있음
     *  Scenario: 상품 목록 정렬
     *      when: "최신 순" 으로 목록을 조회하면
     *      then: 상품 등록일자가 최신인 상품 먼저 조회된다.
     */
    @Test
    @DisplayName("상품 정렬 테스트 - 최신 순")
    void sort_products_by_date() {
        // given
        상품_등록_요청("육류1", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류2", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류3", 15000, 20, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 높은_평점순_목록_조회_결과 = 상품_목록_조회_요청(식품, 육류, ProductQueryOrderType.SCORE);

        // then
        assertThat(높은_평점순_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청(CategoryResponse category, CategoryResponse subCategory, ProductQueryOrderType sortType) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products?categoryId={categoryId}&subCategoryId={subCategoryId}&order={order}", category.getId(),
                        subCategory.getId(),
                        sortType.name())
                .then().log().all()
                .extract();
    }
}
