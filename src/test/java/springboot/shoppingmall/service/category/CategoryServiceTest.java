package springboot.shoppingmall.service.category;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.dto.category.CategoryRequest;
import springboot.shoppingmall.dto.category.CategoryResponse;

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
        Long categoryId = categoryService.saveCategory(categoryRequest);

        // then
        CategoryResponse categoryResponse = categoryService.findCategoryById(categoryId);
        assertThat(categoryResponse.getId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("하위 카테고리 추가")
    void addSubCategoryTest(){
        // given
        CategoryRequest categoryRequest = new CategoryRequest("식품", null);
        Long categoryId = categoryService.saveCategory(categoryRequest);

        // when
        categoryService.saveCategory(new CategoryRequest("육류", categoryId));
        categoryService.saveCategory(new CategoryRequest("생선", categoryId));

        // then
        CategoryResponse categoryResponse = categoryService.findCategoryById(categoryId);
        List<String> categoryNames = categoryResponse.getSubCategories().stream()
                .map(CategoryResponse::getName)
                .collect(Collectors.toList());
        assertThat(categoryNames).containsExactly(
                "육류", "생선"
        );
    }
}
