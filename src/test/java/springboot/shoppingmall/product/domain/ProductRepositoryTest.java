package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 별 상품목록 조회")
    void findProductsByCategoryAndSubCategoryTest(){
        // given
        Category category = categoryRepository.save(new Category("카테고리1"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리1").changeParent(category));
        Product product1 = productRepository.save(new Product("상품1", 120000, 22, category, subCategory));
        Product product2 = productRepository.save(new Product("상품2", 120000, 22, category, subCategory));

        // when
        List<Product> products = productRepository.findProductsByCategoryAndSubCategory(category, subCategory);

        // then
        assertThat(products).hasSize(2);
        assertThat(products).containsExactly(
                product1, product2
        );

    }

}