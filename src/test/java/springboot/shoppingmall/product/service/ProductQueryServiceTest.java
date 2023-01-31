package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import springboot.shoppingmall.product.dto.ProductResponse;

@Transactional
@SpringBootTest
class ProductQueryServiceTest {

    @Autowired
    ProductQueryService productQueryService;

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

        productRepository.save(new Product("청바지", 12000, 10, 2.0, 5, LocalDateTime.of(2022, 12, 23, 12, 0, 0), category, subCategory));
        productRepository.save(new Product("면바지", 17900, 10, 1.7, 2, LocalDateTime.of(2022, 12, 23, 12, 10, 0), category, subCategory));
        productRepository.save(new Product("조거팬츠", 23100, 10, 2.7, 7, LocalDateTime.of(2022, 12, 22, 12, 20, 0), category, subCategory));
        productRepository.save(new Product("반바지", 6900, 10, 3.7, 4, LocalDateTime.of(2022, 12, 23, 11, 10, 0), category, subCategory));
    }

    @Test
    @DisplayName("카테고리 별 상품목록 조회 테스트")
    void findProductsByCategoryTest(){
        // given

        // when
        List<ProductResponse> products = productQueryService.findProductsByCategory(category.getId(),
                subCategory.getId());

        // then
        assertThat(products).hasSize(4);
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 높은 평점 순")
    void sort_score_products_test() {
        // given

        // when
        List<ProductResponse> scoreOrderProducts = productQueryService.findProductByOrder(
                category.getId(), subCategory.getId(), SCORE);

        // then
        assertThat(scoreOrderProducts).hasSize(4);

        List<String> names = scoreOrderProducts.stream()
                .map(ProductResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "반바지", "조거팬츠", "청바지", "면바지"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 최신 순")
    void sort_recent_products_test() {
        // given

        // when
        List<ProductResponse> recentOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), RECENT);

        // then
        assertThat(recentOrderProducts).hasSize(4);

        List<String> names = recentOrderProducts.stream()
                .map(ProductResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "면바지", "청바지", "반바지", "조거팬츠"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 낮은 가격 순")
    void sort_price_products_test() {
        // given

        // when
        List<ProductResponse> priceOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), PRICE);

        // then
        assertThat(priceOrderProducts).hasSize(4);

        List<String> names = priceOrderProducts.stream()
                .map(ProductResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "반바지", "청바지", "면바지", "조거팬츠"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 판매량 높은 순")
    void sort_salesCount_products_test() {
        // given

        // when
        List<ProductResponse> salesOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), SELL);

        // then
        assertThat(salesOrderProducts).hasSize(4);

        List<String> names = salesOrderProducts.stream()
                .map(ProductResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "조거팬츠","청바지","반바지","면바지"
        );
    }

    @Test
    @DisplayName("상품 목록 검색 테스트")
    void search_products_keyword() {
        // given

        // when
        List<ProductResponse> products = productQueryService.searchProducts(category.getId(), subCategory.getId(), "바지");

        // then
        List<String> names = products.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());
        assertThat(names).contains(
                "청바지","반바지","면바지"
        );
    }
}