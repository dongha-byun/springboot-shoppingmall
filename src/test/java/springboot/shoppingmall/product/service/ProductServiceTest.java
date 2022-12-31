package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;

@Transactional
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

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
        ProductResponse productResponse = productService.saveProduct(productRequest);

        // then
        assertThat(productResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("카테고리 별 상품목록 조회 테스트")
    void findProductsByCategoryTest(){
        // given
        productRepository.save(new Product("청바지", 12000, 10, category, subCategory));
        productRepository.save(new Product("청바지", 12000, 10, category, subCategory));

        // when
        List<ProductResponse> products = productService.findProductsByCategory(category.getId(),
                subCategory.getId());

        // then
        assertThat(products).hasSize(2);
    }
}
