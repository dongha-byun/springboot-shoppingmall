package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;

@Transactional
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    Category category;
    Category subCategory;

    @BeforeEach
    void beforeEach(){
        category = categoryRepository.save(new Category("의류"));
        subCategory = categoryRepository.save(new Category("바지").changeParent(category));
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void saveTest(){

        // given
        ProductRequest productRequest = new ProductRequest("청바지", 20000, 100, category.getId(), subCategory.getId());

        // when
        ProductResponse productResponse = productService.saveProduct(100L, productRequest);

        // then
        assertThat(productResponse.getId()).isNotNull();
    }
}
