package springboot.shoppingmall.category.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.category.dto.CategoryRequest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.category.service.CategoryService;

@Transactional
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;


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

    @Test
    @DisplayName("모든 카테고리 조회")
    void findCategoryAll(){
        // given
        CategoryResponse categoryResponse1 = categoryService.saveCategory(new CategoryRequest("상위 카테고리 1", null));
        categoryService.saveCategory(new CategoryRequest("하위 카테고리 1-1", categoryResponse1.getId()));
        categoryService.saveCategory(new CategoryRequest("하위 카테고리 1-2", categoryResponse1.getId()));

        CategoryResponse categoryResponse2 = categoryService.saveCategory(new CategoryRequest("상위 카테고리 2", null));
        categoryService.saveCategory(new CategoryRequest("하위 카테고리 2-1", categoryResponse2.getId()));
        categoryService.saveCategory(new CategoryRequest("하위 카테고리 2-2", categoryResponse2.getId()));
        categoryService.saveCategory(new CategoryRequest("하위 카테고리 2-3", categoryResponse2.getId()));

        // when
        List<CategoryResponse> categories = categoryService.findCategories();

        // then
        List<Long> categoryIds = categories.stream()
                .map(CategoryResponse::getId).collect(Collectors.toList());
        assertThat(categoryIds).contains(
                categoryResponse1.getId(), categoryResponse2.getId()
        );
    }
}
