package springboot.shoppingmall.product.query.service;

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
import springboot.shoppingmall.product.dto.ProductDto;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;

@Transactional
@SpringBootTest
class ProductQueryServiceTest {

    @Autowired
    ProductQueryService productQueryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProviderRepository providerRepository;

    Category category;
    Category subCategory;
    Provider partners;

    @BeforeEach
    void beforeEach(){
        partners = providerRepository.save(
                new Provider("테스트판매사", "테스트대표", "테스트시 테스트구 테스트동", "031-444-1234", "110-22-334411",
                        "test_partner", "test_partner1!", true)
        );
        category = categoryRepository.save(new Category("의류"));
        subCategory = categoryRepository.save(new Category("바지").changeParent(category));

        // "면바지", "청바지", "반바지", "조거팬츠"
        LocalDateTime now = LocalDateTime.now();
        productRepository.save(
                new Product(
                        "조거팬츠", 23100, 10, 2.7, 7, now,
                        category, subCategory, partners.getId(),
                        "storedFileName1", "viewFileName1", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지", 6900, 10, 3.7, 4, now.plusDays(1),
                        category, subCategory, partners.getId(),
                        "storedFileName2", "viewFileName2", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 2", 6900, 10, 3.7, 4, now.plusDays(2),
                        category, subCategory, partners.getId(),
                        "storedFileName3", "viewFileName3", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 3", 6900, 10, 3.7, 4, now.plusDays(3),
                        category, subCategory, partners.getId(),
                        "storedFileName4", "viewFileName4", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 4", 6900, 10, 3.7, 4, now.plusDays(4),
                        category, subCategory, partners.getId(),
                        "storedFileName5", "viewFileName5", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 5", 6900, 10, 3.7, 4, now.plusDays(5),
                        category, subCategory, partners.getId(),
                        "storedFileName6", "viewFileName6", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 6", 6900, 10, 3.7, 4, now.plusDays(6),
                        category, subCategory, partners.getId(),
                        "storedFileName7", "viewFileName7", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 7", 6900, 10, 3.7, 4, now.plusDays(7),
                        category, subCategory, partners.getId(),
                        "storedFileName8", "viewFileName8", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "반바지 8", 6900, 10, 3.7, 4, now.plusDays(8),
                        category, subCategory, partners.getId(),
                        "storedFileName9", "viewFileName9", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "청바지", 12000, 10, 2.0, 5, now.plusDays(9),
                        category, subCategory, partners.getId(),
                        "storedFileName10", "viewFileName10", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "면바지", 17900, 10, 1.7, 2, now.plusDays(10),
                        category, subCategory, partners.getId(),
                        "storedFileName11", "viewFileName11", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "면바지 2", 17900, 10, 1.7, 2, now.plusDays(11),
                        category, subCategory, partners.getId(),
                        "storedFileName12", "viewFileName12", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "면바지 3", 17900, 10, 1.7, 2, now.plusDays(12),
                        category, subCategory, partners.getId(),
                        "storedFileName13", "viewFileName13", "상품 설명 입니다."
                )
        );
        productRepository.save(
                new Product(
                        "면바지 4", 17900, 10, 1.7, 2, now.plusDays(13),
                        category, subCategory, partners.getId(),
                        "storedFileName14", "viewFileName14", "상품 설명 입니다."
                )
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 높은 평점 순")
    void sort_score_products_test() {
        // given

        // when
        List<ProductQueryResponse> scoreOrderProducts = productQueryService.findProductByOrder(
                category.getId(), subCategory.getId(), SCORE);

        // then
        assertThat(scoreOrderProducts).hasSize(14);

        List<String> names = scoreOrderProducts.stream()
                .map(ProductQueryResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "반바지","반바지 2","반바지 3","반바지 4","반바지 5","반바지 6","반바지 7","반바지 8",
                "조거팬츠", "청바지",
                "면바지", "면바지 2","면바지 3","면바지 4"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 최신 순")
    void sort_recent_products_test() {
        // given

        // when
        List<ProductQueryResponse> recentOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), RECENT);

        // then
        assertThat(recentOrderProducts).hasSize(14);

        List<String> names = recentOrderProducts.stream()
                .map(ProductQueryResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "면바지 4", "면바지 3","면바지 2","면바지",
                "청바지",
                "반바지 8","반바지 7","반바지 6","반바지 5","반바지 4","반바지 3","반바지 2","반바지",
                "조거팬츠"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 낮은 가격 순")
    void sort_price_products_test() {
        // given

        // when
        List<ProductQueryResponse> priceOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), PRICE);

        // then
        assertThat(priceOrderProducts).hasSize(14);

        List<String> names = priceOrderProducts.stream()
                .map(ProductQueryResponse::getName).collect(Collectors.toList());
        assertThat(names).containsExactly(
                "반바지","반바지 2","반바지 3","반바지 4","반바지 5","반바지 6","반바지 7","반바지 8",
                "청바지",
                "면바지", "면바지 2","면바지 3","면바지 4",
                "조거팬츠"
        );
    }

    @Test
    @DisplayName("상품목록 정렬 테스트 - 판매량 높은 순")
    void sort_salesCount_products_test() {
        // given

        // when
        List<ProductQueryResponse> salesOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), SELL);

        // then
        assertThat(salesOrderProducts).hasSize(14);

        List<String> names = salesOrderProducts.stream()
                .map(ProductQueryResponse::getName).collect(Collectors.toList());
        assertThat(names).hasSize(14);
        assertThat(names).containsExactly(
                "조거팬츠","청바지",
                "반바지","반바지 2","반바지 3","반바지 4","반바지 5","반바지 6","반바지 7","반바지 8",
                "면바지","면바지 2","면바지 3","면바지 4"
        );
    }

    @Test
    @DisplayName("상품 목록 검색 테스트")
    void search_products_keyword() {
        // given

        // when
        List<ProductQueryDto> products =
                productQueryService.searchProducts("바지", RECENT, 5, 0);

        // then
        List<String> names = products.stream()
                .map(ProductQueryDto::getName)
                .collect(Collectors.toList());
        assertThat(names).contains(
                "면바지 4","면바지 3","면바지 2","면바지","청바지"
        );
    }

    @Test
    @DisplayName("상품 페이징 테스트 - limit 10 offset 10")
    void paging_test() {
        // given

        // when
        // 10번째 부터 10개 조회 -> 11 ~ 14 번째 상품 조회
        List<ProductQueryResponse> products = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), RECENT,  10, 10);

        // then
        assertThat(products).hasSize(4);
        List<String> names = products.stream()
                .map(ProductQueryResponse::getName)
                .collect(Collectors.toList());
        assertThat(names).containsExactly(
                "반바지 3", "반바지 2", "반바지", "조거팬츠"
        );
    }

    @Test
    @DisplayName("총 상품 갯수 조회")
    void total_count_test() {
        // given

        // when
        int totalCount = productQueryService.getTotalCount(category.getId(), subCategory.getId());

        // then
        assertThat(totalCount).isEqualTo(14);
    }
}