package springboot.shoppingmall.cart;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;

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
import springboot.shoppingmall.product.presentation.response.ProductResponse;
import springboot.shoppingmall.cart.presentation.response.CartResponse;

public class CartAcceptanceTest extends AcceptanceTest {
    ProductResponse 상품1;
    ProductResponse 상품2;

    @BeforeEach
    void setUp(){
        super.acceptance_beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품1 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        상품2 = 상품_등록_요청("상품 2", 5000, 50, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
    }

    /**
     * Feature:
     *  background:
     *      given: 사용자가 로그인 되어있고,
     *      And: 상품이 기존에 등록되어 있다.
     *
     *  Scenario: 장바구니 관리
     *      when: 장바구니를 추가하면
     *      then: 목록 조회 시, 장바구니에 추가된 상품이 조회되고,
     *      when: 장바구니에 추가된 상품을 제거하면
     *      then: 목록 조회 시, 제거된 상품은 조회되지 않는다.
     */
    @Test
    @DisplayName("장바구니 관리 테스트")
    void basketTest(){
        // given

        // when 1.
        ExtractableResponse<Response> 장바구니_추가_요청_결과_1 = 장바구니_추가_요청(로그인정보, 상품1, 3);
        ExtractableResponse<Response> 장바구니_추가_요청_결과_2 = 장바구니_추가_요청(로그인정보, 상품2, 1);

        // then 1.
        assertThat(장바구니_추가_요청_결과_1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(장바구니_추가_요청_결과_2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 2.
        CartResponse 장바구니_상품_1 = 장바구니_추가_요청_결과_1.as(CartResponse.class);
        ExtractableResponse<Response> 장바구니_삭제_요청_결과 = 장바구니_삭제_요청(로그인정보, 장바구니_상품_1);
        assertThat(장바구니_삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 2.
        ExtractableResponse<Response> 장바구니_목록_조회_결과 = 장바구니_목록_조회_요청(로그인정보);
        assertThat(장바구니_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     *  given: 기존에 장바구니에 상품이 등록되어 있음
     *  And: 사용자가 로그인되어 있음
     *  when: 사용자가 장바구니 목록을 조회하면
     *  then: 장바구니 목록이 조회된다.
     */
    @Test
    @DisplayName("장바구니 목록 조회 테스트")
    void cart_list_test() {
        // given
        장바구니_추가_요청(로그인정보, 상품1, 3);
        장바구니_추가_요청(로그인정보, 상품2, 1);

        // when
        ExtractableResponse<Response> 장바구니_목록_조회_요청 = 장바구니_목록_조회_요청(로그인정보);

        // then
        assertThat(장바구니_목록_조회_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(장바구니_목록_조회_요청.jsonPath().getList("productName", String.class)).contains(
                상품1.getName(), 상품2.getName()
        );
        assertThat(장바구니_목록_조회_요청.jsonPath().getList("price", Integer.class)).contains(
                상품1.getPrice(), 상품2.getPrice()
        );
    }

    private ExtractableResponse<Response> 장바구니_삭제_요청(TokenResponse token, CartResponse cartResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(token))
                .when().delete("/carts/{id}", cartResponse.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_목록_조회_요청(TokenResponse token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(token))
                .when().get("/carts")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_추가_요청(TokenResponse token, ProductResponse product, int quantity) {
        Map<String, Object> body = new HashMap<>();
        body.put("quantity", quantity);
        body.put("productId", product.getId());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(token))
                .body(body)
                .when().post("/carts")
                .then().log().all()
                .extract();
    }
}
