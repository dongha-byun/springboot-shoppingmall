package springboot.shoppingmall.api.category;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.dto.category.CategoryResponse;

public class CategoryAcceptanceTest extends AcceptanceTest {

    /**
     * given 카테고리 정보를 입력하고
     * when 카테고리를 등록하면
     * then 카테고리 조회 시, 등록된 카테고리가 조회된다.
     */
    @Test
    @DisplayName("카테고리 등록 테스트")
    void addCategory(){
        // given

        // when
        ExtractableResponse<Response> 등록_응답 = 카테고리_등록("식품", null);
        assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> 조회_응답 = 카테고리_조회(등록_응답.as(Long.class));
        assertThat(조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given 카테고리 하나를 등록하고
     * when 등록된 카테고리에 하위 카테고리를 등록하면
     * then 카테고리 조회 시, 하위 카테고리가 같이 조회된다.
     */
    @Test
    @DisplayName("하위 카테고리 등록 테스트")
    void addSubCategory(){
        // given
        ExtractableResponse<Response> 상위_등록_응답 = 카테고리_등록("식품", null);
        assertThat(상위_등록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> 하위_등록_응답1 = 카테고리_등록("육류", 상위_등록_응답.as(Long.class));
        assertThat(하위_등록_응답1.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 하위_등록_응답2 = 카테고리_등록("생선", 상위_등록_응답.as(Long.class));
        assertThat(하위_등록_응답2.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> 조회_응답 = 카테고리_조회(상위_등록_응답.as(Long.class));
        CategoryResponse response = 조회_응답.as(CategoryResponse.class);
        List<String> names = response.getSubCategories().stream()
                .map(CategoryResponse::getName)
                .collect(Collectors.toList());
        assertThat(조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).containsExactly(
                "육류", "생선"
        );
    }

    private ExtractableResponse<Response> 카테고리_조회(Long id) {
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("x-auth-token", "testToken");
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headerParam)
                .when().get("/category/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 카테고리_등록(String name, Long parentId) {
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("x-auth-token", "testToken");

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("parentId", parentId);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headerParam)
                .body(params)
                .when().post("/category")
                .then().log().all()
                .extract();
        return response;
    }
}
