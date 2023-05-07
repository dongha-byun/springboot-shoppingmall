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
import springboot.shoppingmall.product.dto.ProductDto;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;

@Transactional
@SpringBootTest
class ProductQueryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductQueryRepository productQueryRepository;

    @Autowired
    ProviderRepository providerRepository;

    Category category;
    Category subCategory;

    Product 생선1;
    Product 생선2;
    Product 생선3;

    LocalDateTime now;
    Provider partners;

    @BeforeEach
    void setUp() {
        partners = providerRepository.save(
                new Provider("테스트판매사", "테스트대표", "테스트시 테스트구 테스트동", "031-444-1234", "110-22-334411",
                        "test_partner", "test_partner1!", true)
        );
        category = categoryRepository.save(new Category("식품 분류"));
        subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        now = LocalDateTime.now();
        생선1 = productRepository.save(
                new Product("생선1", 1000, 10, 1.0, 10, now, category, subCategory, partners.getId())
        );
        생선2 = productRepository.save(
                new Product("생선2", 1200, 11, 1.5, 20, now.plusDays(1), category, subCategory, partners.getId())
        );
        생선3 = productRepository.save(
                new Product("생선3", 1500, 12, 3.0, 15, now.plusDays(2), category, subCategory, partners.getId())
        );
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
                "생선3", "생선2", "생선1"
        );
    }

    @Test
    @DisplayName("상품 목록 조회 테스트 - 낮은 가격 순")
    void sort_product_by_price() {
        // given

        // when
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, PRICE);

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
        List<ProductQueryDto> productDtos =
                productQueryRepository.searchProducts("생선", RECENT, 10, 0);

        // then
        assertThat(productDtos).hasSize(3);
        List<Long> ids = productDtos.stream()
                .map(ProductQueryDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                생선3.getId(), 생선2.getId(), 생선1.getId()
        );

    }

    @Test
    @DisplayName("상품 페이징 테스트 - 7개 중 뒤에 3개 조회(limit : 3 / offset : 4)")
    void paging_test() {
        // given
        saveMoreProducts();

        // when
        // 7 개 중 뒤에 3개 조회이므로, 가장 나중에 등록된 3개가 최신순으로 조회되면 됨
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, RECENT, 3, 4);

        // then
        assertThat(products).hasSize(3);
        List<Long> ids = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                생선3.getId(), 생선2.getId(), 생선1.getId()
        );
    }

    @Test
    @DisplayName("총 상품 갯수 조회")
    void total_count_test() {
        // given

        // when
        int totalCount = productQueryRepository.countByCategoryAndSubCategory(category, subCategory);

        // then
        assertThat(totalCount).isEqualTo(3);
    }

    @Test
    @DisplayName("판매자가 등록한 상품 목록조회")
    void find_all_partners_products() {
        // given
        Long partnerId = 10L;
        Product product1 = productRepository.save(
                new Product("product1", 1500, 12, 3.0, 15, now.plusDays(3), category, subCategory, partnerId)
        );
        Product product2 = productRepository.save(
                new Product("product2", 1500, 12, 3.0, 15, now.plusDays(2), category, subCategory, partnerId)
        );
        Product product3 = productRepository.save(
                new Product("product3", 1500, 12, 3.0, 15, now.plusDays(1), category, subCategory, partnerId)
        );
        Product product4 = productRepository.save(
                new Product("product4", 1500, 12, 3.0, 15, now, category, subCategory, partnerId)
        );

        // when
        List<Product> products = productQueryRepository.queryPartnersProducts(partnerId, category, subCategory, 10, 0);

        // then
        assertThat(products).hasSize(4);
        List<Long> ids = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                product1.getId(), product2.getId(), product3.getId(), product4.getId()
        );
    }

    @Test
    @DisplayName("판매자가 등록한 상품의 갯수 조회")
    void count_partners_products() {
        // given
        Long partnerId = 10L;
        productRepository.save(
                new Product("product1", 1500, 12, 3.0, 15, now.plusDays(3), category, subCategory, partnerId)
        );
        productRepository.save(
                new Product("product2", 1500, 12, 3.0, 15, now.plusDays(2), category, subCategory, partnerId)
        );
        productRepository.save(
                new Product("product3", 1500, 12, 3.0, 15, now.plusDays(1), category, subCategory, partnerId)
        );
        productRepository.save(
                new Product("product4", 1500, 12, 3.0, 15, now, category, subCategory, partnerId)
        );

        // when
        int count = productQueryRepository.countByPartnerIdAndCategoryAndSubCategory(partnerId, category, subCategory);

        // then
        assertThat(count).isEqualTo(4);
    }

    private void saveMoreProducts() {
        productRepository.save(
                new Product("생선4", 1500, 12, 3.0, 15, now.plusDays(3),
                        category, subCategory));
        productRepository.save(
                new Product("생선5", 1500, 12, 3.0, 15, now.plusDays(4),
                        category, subCategory));
        productRepository.save(
                new Product("생선6", 1500, 12, 3.0, 15, now.plusDays(5),
                        category, subCategory));
        productRepository.save(
                new Product("생선7", 1500, 12, 3.0, 15, now.plusDays(6),
                        category, subCategory));
    }
}