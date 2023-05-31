package springboot.shoppingmall.providers;

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

public class PartnersProductQueryAcceptanceTest extends AcceptanceTest {

    CategoryResponse 상위_카테고리;
    CategoryResponse 하위_카테고리;
    @BeforeEach
    void acceptance_partners_product_beforeEach() {
        상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
    }

    /**
     *  given: 판매자가 판매 승인 처리가 되어있음
     *  And: 판매를 위해 등록해놓은 상품이 여러개 존재한다.
     *  when: 등록한 상품 목록을 조회하면
     *  then: 상품 목록이 조회된다.
     */
    @Test
    @DisplayName("판매자가 등록한 상품 목록을 조회한다.")
    void partners_products_find_all() {
        // given
        Long categoryId = 상위_카테고리.getId();
        Long subCategoryId = 하위_카테고리.getId();
        상품_등록_요청("상품 추가 등록 1", 12000, 100, 상위_카테고리.getId(), 하위_카테고리.getId());
        상품_등록_요청("상품 추가 등록 2", 13000, 150, 상위_카테고리.getId(), 하위_카테고리.getId());
        상품_등록_요청("상품 추가 등록 3", 14000, 200, 상위_카테고리.getId(), 하위_카테고리.getId());

        // when
        ExtractableResponse<Response> 판매자_상품_목록_조회_요청 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().get("/partners/products"
                        + "?categoryId={categoryId}"
                        + "&subCategoryId={subCategoryId}"
                        + "&limit={limit}"
                        + "&offset={offset}",
                        categoryId,
                        subCategoryId,
                        10,
                        0)
                .then().log().all()
                .extract();

        // then
        assertThat(판매자_상품_목록_조회_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(판매자_상품_목록_조회_요청.jsonPath().getList("data.id")).hasSize(3);
        assertThat(판매자_상품_목록_조회_요청.jsonPath().getList("data.name", String.class)).containsExactly(
                "상품 추가 등록 3", "상품 추가 등록 2", "상품 추가 등록 1"
        );
        assertThat(판매자_상품_목록_조회_요청.jsonPath().getList("data.price", Integer.class)).containsExactly(
                14000, 13000, 12000
        );
        assertThat(판매자_상품_목록_조회_요청.jsonPath().getList("data.count", Integer.class)).containsExactly(
                200, 150, 100
        );
    }
}
