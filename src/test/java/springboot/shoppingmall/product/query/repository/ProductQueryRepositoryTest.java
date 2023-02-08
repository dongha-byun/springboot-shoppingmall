package springboot.shoppingmall.product.query.repository;

import static org.assertj.core.api.Assertions.*;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.*;

import java.time.LocalDateTime;
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
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

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

    Product 생선1;
    Product 생선2;
    Product 생선3;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(new Category("식품 분류"));
        subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        생선1 = productRepository.save(
                new Product("생선1", 1000, 10, 1.0, 10, LocalDateTime.of(2022, 12, 22, 19, 30, 0),
                        category, subCategory));
        생선2 = productRepository.save(
                new Product("생선2", 1200, 11, 1.5, 20, LocalDateTime.of(2022, 12, 22, 19, 20, 0),
                        category, subCategory));
        생선3 = productRepository.save(
                new Product("생선3", 1500, 12, 3.0, 15, LocalDateTime.of(2022, 12, 22, 19, 10, 0),
                        category, subCategory));
    }

    @Test
    @DisplayName("상품 목록 조회 테스트 - 평점 높은 순")
    void sort_product_by_score() {
        // given

        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, SCORE);

        // then
        assertThat(products).hasSize(3);
        List<String> names = products.stream()
                .map(Product::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "생선3", "생선2", "생선1"
        );
    }

    @Test
    @DisplayName("상품 목록 조회 테스트 - 최신 순")
    void sort_product_by_date() {
        // given

        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, RECENT);

        // then
        assertThat(products).hasSize(3);

        List<String> names = products.stream()
                .map(Product::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "생선1", "생선2", "생선3"
        );
    }

    @Test
    @DisplayName("상품 목록 조회 테스트 - 낮은 가격 순")
    void sort_product_by_price() {
        // given

        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, RECENT);

        // then
        assertThat(products).hasSize(3);
        List<String> names = products.stream()
                .map(Product::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "생선1", "생선2", "생선3"
        );
    }

    @Test
    @DisplayName("상품 목록 조회 테스트 - 판매량 높은 순")
    void sort_product_by_salesCount() {
        // given

        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, SELL);

        // then
        assertThat(products).hasSize(3);
        List<String> names = products.stream()
                .map(Product::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "생선2", "생선3", "생선1"
        );
    }

    @Test
    @DisplayName("상품 검색 테스트 - 검색 시, 정렬 초기화")
    void search_products_no_order() {
        // given

        // when
        List<Product> products = productQueryRepository.searchProducts(category, subCategory, "선1");

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("생선1");
    }
}