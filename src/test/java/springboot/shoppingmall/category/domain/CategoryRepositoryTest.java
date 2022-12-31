package springboot.shoppingmall.category.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;


    @BeforeEach
    void setUp(){
        Category parent = categoryRepository.save(new Category("상위 카테고리 1"));
        categoryRepository.save(new Category("하위 카테고리1").changeParent(parent));
        categoryRepository.save(new Category("하위 카테고리2").changeParent(parent));

        Category parent2 = categoryRepository.save(new Category("상위 카테고리 2"));
        categoryRepository.save(new Category("하위 카테고리3").changeParent(parent2));
        categoryRepository.save(new Category("하위 카테고리4").changeParent(parent2));
    }

    @Test
    @DisplayName("상위 카테고리 조회 테스트")
    void parentCategoryTest(){
        // given

        // when
        List<Category> parentCategories = categoryRepository.findParentCategoryAll();

        // then
        assertThat(parentCategories).hasSize(2);
    }
}