package springboot.shoppingmall.category.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.dto.CategoryRequest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.category.service.CategoryService;

@Transactional
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("카테고리 추가")
    void addCategoryTest(){
        // given
        CategoryRequest categoryRequest = new CategoryRequest("식품", null);

        // when
        CategoryResponse categoryResponse = categoryService.saveCategory(categoryRequest);

        // then
        assertThat(categoryResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("하위 카테고리 추가")
    void addSubCategoryTest(){
        // given
        CategoryRequest categoryRequest = new CategoryRequest("식품", null);
        CategoryResponse categoryResponse = categoryService.saveCategory(categoryRequest);

        // when
        categoryService.saveCategory(new CategoryRequest("육류", categoryResponse.getId()));
        categoryService.saveCategory(new CategoryRequest("생선", categoryResponse.getId()));

        // then
        CategoryResponse response = categoryService.findCategoryById(categoryResponse.getId());
        List<String> categoryNames = response.getSubCategories().stream()
                .map(CategoryResponse::getName)
                .collect(Collectors.toList());
        assertThat(categoryNames).containsExactly(
                "육류", "생선"
        );
    }
}
