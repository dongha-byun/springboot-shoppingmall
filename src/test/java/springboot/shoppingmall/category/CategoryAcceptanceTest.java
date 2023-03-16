package springboot.shoppingmall.category;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceTest;
import springboot.shoppingmall.category.dto.CategoryResponse;

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
        assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> 조회_응답 = 카테고리_조회(등록_응답);
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
        assertThat(상위_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        CategoryResponse categoryResponse = 상위_등록_응답.as(CategoryResponse.class);

        ExtractableResponse<Response> 하위_등록_응답1 = 카테고리_등록("육류", categoryResponse.getId());
        assertThat(하위_등록_응답1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> 하위_등록_응답2 = 카테고리_등록("생선", categoryResponse.getId());
        assertThat(하위_등록_응답2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> 조회_응답 = 카테고리_조회(상위_등록_응답);
        CategoryResponse response = 조회_응답.as(CategoryResponse.class);
        List<String> names = response.getSubCategories().stream()
                .map(CategoryResponse::getName)
                .collect(Collectors.toList());
        assertThat(조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).containsExactly(
                "육류", "생선"
        );
    }

    /**
     * given: 기존에 카테고리(상위1 : 하위1-1,하위1-2 / 상위2 : 하위2-1,하위2-2,하위2-3) 가 생성되어 있고,
     * when: 카테고리를 전체 조회 하면,
     * then: 카테고리 전체 목록이 조회된다.
     */
    @Test
    @DisplayName("전체 카테고리 목록 조회")
    void find_categories_test() {
        // given
        CategoryResponse 상위_카테고리_1 = 카테고리_등록("상위1", null).as(CategoryResponse.class);
        카테고리_등록("하위1-1", 상위_카테고리_1.getId());
        카테고리_등록("하위1-2", 상위_카테고리_1.getId());

        CategoryResponse 상위_카테고리_2 = 카테고리_등록("상위2", null).as(CategoryResponse.class);
        카테고리_등록("하위2-1", 상위_카테고리_2.getId());
        카테고리_등록("하위2-2", 상위_카테고리_2.getId());
        카테고리_등록("하위2-3", 상위_카테고리_2.getId());

        // when
        ExtractableResponse<Response> 전체_카테고리_조회_결과 = 전체_카테고리_조회_요청();

        // then
        전체_카테고리_조회_성공_검증(전체_카테고리_조회_결과);

        List<CategoryResponse> 상위_카테고리_전체 = 전체_카테고리_조회_결과.jsonPath().getList("data", CategoryResponse.class);
        카테고리_갯수_검증(상위_카테고리_전체, 2);
        카테고리_목록_검증(상위_카테고리_전체, 상위_카테고리_1, 상위_카테고리_2);

        카테고리_유무_갯수_검증(상위_카테고리_전체, 상위_카테고리_1, 2);
        카테고리_유무_갯수_검증(상위_카테고리_전체, 상위_카테고리_2, 3);
    }

    private void 카테고리_목록_검증(List<CategoryResponse> 카테고리_목록, CategoryResponse... 대상_카테고리) {
        List<Long> ids = getIdsInList(카테고리_목록);
        List<CategoryResponse> targetCategories = Arrays.asList(대상_카테고리);
        List<Long> targetCategoryIds = getIdsInList(targetCategories);

        assertThat(ids).containsAll(targetCategoryIds);
    }

    private List<Long> getIdsInList(List<CategoryResponse> targetCategories) {
        return targetCategories.stream()
                .map(CategoryResponse::getId)
                .collect(Collectors.toList());
    }

    private void 카테고리_갯수_검증(List<CategoryResponse> categories, int categoryCount) {
        assertThat(categories).hasSize(categoryCount);
    }

    private void 전체_카테고리_조회_성공_검증(ExtractableResponse<Response> 전체_카테고리_조회_결과) {
        assertThat(전체_카테고리_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 전체_카테고리_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/categories")
                .then().log().all()
                .extract();
    }

    private void 카테고리_유무_갯수_검증(List<CategoryResponse> data, CategoryResponse category, int subCategoryCount) {
        CategoryResponse parentCategory = data.stream()
                .filter(categoryResponse -> categoryResponse.getId().equals(category.getId()))
                .findAny()
                .orElseThrow();

        카테고리_갯수_검증(parentCategory.getSubCategories(), subCategoryCount);
    }

    public static ExtractableResponse<Response> 카테고리_조회(ExtractableResponse<Response> response) {
        CategoryResponse categoryResponse = response.as(CategoryResponse.class);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/category/{id}", categoryResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 카테고리_등록(String name, Long parentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("parentId", parentId);

        return  RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/category")
                .then().log().all()
                .extract();
    }
}
