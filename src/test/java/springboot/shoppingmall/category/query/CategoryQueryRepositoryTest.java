package springboot.shoppingmall.category.query;

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

@Transactional
@SpringBootTest
class CategoryQueryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryQueryRepository categoryQueryRepository;

    @Test
    @DisplayName("카테고리 쿼리 테스트")
    void findCategoryAll(){
        // given
        Category category1 = categoryRepository.save(new Category("상위 카테고리"));
        categoryRepository.save(new Category("하위 카테고리 1").changeParent(category1));
        categoryRepository.save(new Category("하위 카테고리 2").changeParent(category1));

        Category category2 = categoryRepository.save(new Category("상위 카테고리 2"));
        categoryRepository.save(new Category("하위 카테고리 3").changeParent(category2));
        categoryRepository.save(new Category("하위 카테고리 4").changeParent(category2));
        categoryRepository.save(new Category("하위 카테고리 5").changeParent(category2));

        List<CategoryQueryDto> categoryAll = categoryQueryRepository.findCategoryAll();
        List<String> categoryNames = categoryAll.stream()
                .map(CategoryQueryDto::getParentName).collect(Collectors.toList());

        assertThat(categoryNames).contains(
                "상위 카테고리", "상위 카테고리 2"
        );
    }
}