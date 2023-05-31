package springboot.shoppingmall.category.query;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.category.dto.CategoryDto;

@Transactional
@SpringBootTest
class CategoryQueryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryQueryRepository categoryQueryRepository;

    Category category1;
    Category subCategory1;
    Category subCategory2;
    Category category2;
    Category subCategory3;
    Category subCategory4;
    Category subCategory5;
    @BeforeEach
    void beforeEach() {
        category1 = categoryRepository.save(new Category("상위 카테고리"));
        subCategory1 = categoryRepository.save(new Category("하위 카테고리 1").changeParent(category1));
        subCategory2 = categoryRepository.save(new Category("하위 카테고리 2").changeParent(category1));

        category2 = categoryRepository.save(new Category("상위 카테고리 2"));
        subCategory3 = categoryRepository.save(new Category("하위 카테고리 3").changeParent(category2));
        subCategory4 = categoryRepository.save(new Category("하위 카테고리 4").changeParent(category2));
        subCategory5 = categoryRepository.save(new Category("하위 카테고리 5").changeParent(category2));
    }

    @Test
    @DisplayName("상위 카테고리 조회 - dto 버전")
    void find_parent_dto_test() {
        // given

        // when
        List<CategoryDto> parentsDto = categoryQueryRepository.findParentsDto();
        List<Long> parentIds = parentsDto.stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toList());

        // then
        assertThat(parentIds).containsExactly(
                category1.getId(), category2.getId()
        );
    }

    @Test
    @DisplayName("하위 카테고리 조회 - dto 버전")
    void find_children_dto_test() {
        // given
        List<CategoryDto> parents = categoryQueryRepository.findParentsDto();
        List<Long> ids = parents.stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toList());

        // when
        List<CategoryDto> childrenAll = categoryQueryRepository.findSubCategoryDto(ids);
        List<Long> childrenIds = childrenAll.stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toList());

        // then
        assertThat(childrenIds).containsExactly(
                subCategory1.getId(), subCategory2.getId(), subCategory3.getId(), subCategory4.getId(), subCategory5.getId()
        );
    }
}