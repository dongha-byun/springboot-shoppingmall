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
        assertThat(높은_평점순_목록_조회_결과.jsonPath().getString("categoryName")).isEqualTo("식품");
        assertThat(높은_평점순_목록_조회_결과.jsonPath().getString("subCategoryName")).isEqualTo("육류");
        assertThat(높은_평점순_목록_조회_결과.jsonPath().getList("data.name")).containsExactly(
                "육류1", "육류2", "육류3"
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
        ExtractableResponse<Response> 최신순_목록_조회_결과 = 상품_목록_조회_요청(식품, 육류, ProductQueryOrderType.RECENT);

        // then
        assertThat(최신순_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(최신순_목록_조회_결과.jsonPath().getString("categoryName")).isEqualTo("식품");
        assertThat(최신순_목록_조회_결과.jsonPath().getString("subCategoryName")).isEqualTo("육류");
        assertThat(최신순_목록_조회_결과.jsonPath().getList("data.name")).containsExactly(
                "육류3", "육류2", "육류1"
        );
    }

    /**
     * Feature: 상품목록 정렬 테스트
     *  Background:
     *      given: 하나의 카테고리에 여러 개의 등록된 상품들이 있음
     *  Scenario: 상품 목록 정렬
     *      when: "낮은 가격 순" 으로 목록을 조회하면
     *      then: 상품 가격이 낮은 상품 먼저 조회된다.
     */
    @Test
    @DisplayName("상품 정렬 테스트 - 낮은 가격 순")
    void sort_products_by_price() {
        // given
        상품_등록_요청("육류1", 13000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류2", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류3", 19000, 20, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 낮은_가격순_목록_조회_결과 = 상품_목록_조회_요청(식품, 육류, ProductQueryOrderType.PRICE);

        // then
        assertThat(낮은_가격순_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(낮은_가격순_목록_조회_결과.jsonPath().getString("categoryName")).isEqualTo("식품");
        assertThat(낮은_가격순_목록_조회_결과.jsonPath().getString("subCategoryName")).isEqualTo("육류");
        assertThat(낮은_가격순_목록_조회_결과.jsonPath().getList("data.name")).containsExactly(
                "육류1", "육류2", "육류3"
        );
    }

    /**
     *  given: 상품들이 등록되어 있음.
     *  when: 특정 페이지의 목록을 조회하면
     *  then: 해당 페이지에 맞는 상품목록을 조회한다.
     */
    @Test
    @DisplayName("상품 목록 조회 테스트 - 페이징")
    void paging_products_test() {
        // given
        상품_등록_요청("육류01", 10000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류02", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류03", 19000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류04", 21000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류05", 21900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류06", 31200, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류07", 31500, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류08", 41100, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류09", 59900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류10", 69100, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류11", 88000, 20, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 페이지_요청_결과 = 상품_목록_조회_요청_페이징(식품, 육류, ProductQueryOrderType.PRICE, 1, 10);

        // then
        assertThat(페이지_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(페이지_요청_결과.jsonPath().getInt("totalCount")).isEqualTo(11);
        assertThat(페이지_요청_결과.jsonPath().getString("categoryName")).isEqualTo("식품");
        assertThat(페이지_요청_결과.jsonPath().getString("subCategoryName")).isEqualTo("육류");
        assertThat(페이지_요청_결과.jsonPath().getString("data[0].name")).isEqualTo("육류11");
    }

    /**
     *  given: 상품들이 등록되어 있음.
     *  when: 다음 페이지의 목록을 추가로 조회하면
     *  then: 목록 데이터만 조회한다.
     */
    @Test
    @DisplayName("상품 목록 조회 테스트 - 더보기로 상품 목록을 조회할 경우, 카테고리명과 totalCount 는 조회하지 않는다.")
    void paging_more_products_test() {
        // given
        상품_등록_요청("육류01", 10000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류02", 15000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류03", 19000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류04", 21000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류05", 21900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류06", 31200, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류07", 31500, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류08", 41100, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류09", 59900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류10", 69100, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류11", 88000, 20, 식품.getId(), 육류.getId());

        // when
        ExtractableResponse<Response> 페이지_요청_결과 =
                상품_목록_더보기_조회_요청(식품, 육류, ProductQueryOrderType.PRICE, 5, 4); // 5~9

        // then
        assertThat(페이지_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((Integer) 페이지_요청_결과.jsonPath().get("totalCount")).isNull();
        assertThat(페이지_요청_결과.jsonPath().getString("categoryName")).isNull();
        assertThat(페이지_요청_결과.jsonPath().getString("subCategoryName")).isNull();
        assertThat(페이지_요청_결과.jsonPath().getList("data.name", String.class)).containsExactly(
                "육류05","육류06","육류07","육류08","육류09"
        );
    }

    /**
     *  given: 여러 개의 상품이 등록되어 있음
     *  when: 상품명을 검색하면
     *  then: 검색결과가 조회된다.
     */
    @Test
    @DisplayName("상품명 검색 결과 조회 테스트")
    void search_product_test() {
        // given
        상품_등록_요청("육류01", 10000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류02", 19000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류03", 21900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류04", 31500, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류05", 59900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류06", 88000, 20, 식품.getId(), 육류.getId());

        상품_등록_요청("생선01", 15000, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선02", 21000, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선03", 31200, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선04", 41100, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선05", 69100, 20, 식품.getId(), 생선.getId());

        // when
        String searchKeyword = "육류";
        ExtractableResponse<Response> 검색_결과 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/search-products"
                        + "?searchKeyword={searchKeyword}"
                        + "&orderType={orderType}"
                        + "&limit={limit}"
                        + "&offset={offset}",
                        searchKeyword,
                        ProductQueryOrderType.PRICE.name(),
                        3,
                        1)
                .then().log().all()
                .extract();

        // then
        assertThat(검색_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(검색_결과.jsonPath().getList("data.name")).containsExactly(
                "육류02", "육류03", "육류04"
        );
    }

    /**
     *  given: 여러 개의 상품이 등록되어 있음
     *  when: 상품명을 검색하고 더보기를 클릭하면
     *  then: 추가로 검색결과가 조회된다.
     */
    @Test
    @DisplayName("상품명 검색 결과 더보기 테스트")
    void search_product_more_test() {
        // given
        상품_등록_요청("육류01", 10000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류02", 19000, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류03", 21900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류04", 31500, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류05", 59900, 20, 식품.getId(), 육류.getId());
        상품_등록_요청("육류06", 88000, 20, 식품.getId(), 육류.getId());

        상품_등록_요청("생선01", 15000, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선02", 21000, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선03", 31200, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선04", 41100, 20, 식품.getId(), 생선.getId());
        상품_등록_요청("생선05", 69100, 20, 식품.getId(), 생선.getId());

        // when
        String searchKeyword = "육류";
        ExtractableResponse<Response> 검색_결과 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/search-products-more"
                                + "?searchKeyword={searchKeyword}"
                                + "&orderType={orderType}"
                                + "&limit={limit}"
                                + "&offset={offset}",
                        searchKeyword,
                        ProductQueryOrderType.PRICE.name(),
                        3,
                        4)
                .then().log().all()
                .extract();

        // then
        assertThat(검색_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(검색_결과.jsonPath().getList("data.name")).containsExactly(
                "육류05", "육류06"
        );
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청(CategoryResponse category, CategoryResponse subCategory, ProductQueryOrderType sortType) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products"
                                + "?categoryId={categoryId}"
                                + "&subCategoryId={subCategoryId}"
                                + "&orderType={orderType}",
                        category.getId(),
                        subCategory.getId(),
                        sortType.name())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청_페이징(CategoryResponse category, CategoryResponse subCategory,
                                                          ProductQueryOrderType sortType, int limit, int offset) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products"
                                + "?categoryId={categoryId}"
                                + "&subCategoryId={subCategoryId}"
                                + "&orderType={orderType}"
                                + "&limit={limit}"
                                + "&offset={offset}",
                        category.getId(),
                        subCategory.getId(),
                        sortType.name(),
                        limit,
                        offset
                )
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_목록_더보기_조회_요청(CategoryResponse category, CategoryResponse subCategory,
                                                          ProductQueryOrderType sortType, int limit, int offset) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products-more"
                                + "?categoryId={categoryId}"
                                + "&subCategoryId={subCategoryId}"
                                + "&orderType={orderType}"
                                + "&limit={limit}"
                                + "&offset={offset}",
                        category.getId(),
                        subCategory.getId(),
                        sortType.name(),
                        limit,
                        offset
                )
                .then().log().all()
                .extract();
    }
}
