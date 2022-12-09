package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.dto.CategoryRequest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.category.service.CategoryService;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;

@Transactional
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    CategoryResponse category;
    CategoryResponse subCategory;

    @BeforeEach
    void beforeEach(){
        category = categoryService.saveCategory(new CategoryRequest("의류", null));
        subCategory = categoryService.saveCategory(new CategoryRequest("바지", category.getId()));
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void saveTest(){

        // given
        ProductRequest productRequest = new ProductRequest("청바지", 20000, 100, category.getId(), subCategory.getId());

        // when
        ProductResponse productResponse = productService.saveProduct(productRequest);

        // then
        assertThat(productResponse.getId()).isNotNull();
    }
}
