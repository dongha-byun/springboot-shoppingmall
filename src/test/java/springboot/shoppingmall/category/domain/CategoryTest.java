package springboot.shoppingmall.category.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    @DisplayName("하위 카테고리 추가 테스트")
    void addSubCategoryTest(){
        // given
        Category category = new Category("상위 1");
        Category category1 = new Category("하위 1").changeParent(category);

        // when
        category.addSubCategory(category1);

        // then
        assertThat(category.getSubCategories()).hasSize(1);
    }

}