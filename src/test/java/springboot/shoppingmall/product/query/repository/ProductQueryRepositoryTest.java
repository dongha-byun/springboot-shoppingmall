package springboot.shoppingmall.product.query.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

@Transactional
@SpringBootTest
class ProductQueryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductQueryRepository productQueryRepository;

    Category category;
    Category subCategory;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(new Category("식품 분류"));
        subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        productRepository.save(new Product("생선1", 1000, 10, 1.0, LocalDateTime.of(2022, 12, 22, 19, 30, 0), category, subCategory));
        productRepository.save(new Product("생선2", 1200, 11, 1.5, LocalDateTime.of(2022, 12, 22, 19, 20, 0), category, subCategory));
        productRepository.save(new Product("생선3", 1500, 12, 3.0, LocalDateTime.of(2022, 12, 22, 19, 10, 0), category, subCategory));
    }

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 테스트")
    void findProductsByCategory() {
        // given


        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory);

        // then
        assertThat(products).hasSize(3);
    }

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 테스트 - 평점 높은 순")
    void findProductsByScoreSort() {
        // given


        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, ProductQueryOrderType.SCORE);

        // then
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getName()).isEqualTo("생선3");
        assertThat(products.get(0).getScore()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 테스트 - 최신 순")
    void findProductsByDateSort() {
        // given


        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, ProductQueryOrderType.RECENT);

        // then
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getName()).isEqualTo("생선1");
        assertThat(products.get(0).getScore()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 테스트 - 낮은 가격 순")
    void findProductsByPriceSort() {
        // given


        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, ProductQueryOrderType.RECENT);

        // then
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getName()).isEqualTo("생선1");
        assertThat(products.get(0).getPrice()).isEqualTo(1000);
        assertThat(products.get(2).getName()).isEqualTo("생선3");
        assertThat(products.get(2).getPrice()).isEqualTo(1500);
    }
}