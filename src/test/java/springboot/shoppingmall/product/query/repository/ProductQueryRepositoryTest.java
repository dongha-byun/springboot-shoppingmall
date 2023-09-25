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

    Category category, subCategory;

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
        now = LocalDateTime.of(2023, 8, 11, 15, 45, 11);

        saveProduct("생선3", 1500, 12, 3.0, 15, now.minusDays(2));
        saveProduct("생선1", 1000, 10, 1.0, 10, now);
        saveProduct("생선2", 1200, 11, 1.5, 20, now.plusDays(1));
        saveProduct("생선4", 2300, 9, 2.0, 8, now.plusDays(3));
        saveProduct("생선5", 9900, 1, 2.5, 11, now.plusDays(4));
        saveProduct("생선6", 3900, 3, 3.5, 30, now.plusDays(5));
        saveProduct("생선7", 1350, 7, 4.8, 2, now.plusDays(6));
    }

    @Test
    @DisplayName("상품목록을 평점이 높은 순으로 조회한다.")
    void sort_product_by_score() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryProducts(category, subCategory, SCORE);

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "score")
                .containsExactly(
                        tuple("생선7", 4.8),
                        tuple("생선6", 3.5),
                        tuple("생선3", 3.0),
                        tuple("생선5", 2.5),
                        tuple("생선4", 2.0),
                        tuple("생선2", 1.5),
                        tuple("생선1", 1.0)
                );
    }

    @Test
    @DisplayName("상품목록을 최신순으로 조회한다.")
    void sort_product_by_date() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryProducts(category, subCategory, RECENT);

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("생선7", LocalDateTime.of(2023, 8, 17, 15, 45, 11)),
                        tuple("생선6", LocalDateTime.of(2023, 8, 16, 15, 45, 11)),
                        tuple("생선5", LocalDateTime.of(2023, 8, 15, 15, 45, 11)),
                        tuple("생선4", LocalDateTime.of(2023, 8, 14, 15, 45, 11)),
                        tuple("생선2", LocalDateTime.of(2023, 8, 12, 15, 45, 11)),
                        tuple("생선1", LocalDateTime.of(2023, 8, 11, 15, 45, 11)),
                        tuple("생선3", LocalDateTime.of(2023, 8, 9, 15, 45, 11))
                );
    }

    @Test
    @DisplayName("상품목록을 낮은 가격 순으로 조회한다.")
    void sort_product_by_price() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryProducts(category, subCategory, PRICE);

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "price")
                .containsExactly(
                        tuple("생선1", 1000),
                        tuple("생선2", 1200),
                        tuple("생선7", 1350),
                        tuple("생선3", 1500),
                        tuple("생선4", 2300),
                        tuple("생선6", 3900),
                        tuple("생선5", 9900)
                );
    }

    @Test
    @DisplayName("상품목록을 판매량이 높은 순으로 조회한다.")
    void sort_product_by_salesCount() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryProducts(category, subCategory, SELL);

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "salesVolume")
                .containsExactly(
                        tuple("생선6", 30),
                        tuple("생선2", 20),
                        tuple("생선3", 15),
                        tuple("생선5", 11),
                        tuple("생선1", 10),
                        tuple("생선4", 8),
                        tuple("생선7", 2)
                );
    }

    @Test
    @DisplayName("상품을 상품명으로 검색한다.")
    void search_products_no_order() {
        // given

        // when
        List<ProductQueryDto> products =
                productQueryRepository.searchProducts("생선", RECENT, 5, 0);

        // then
        assertThat(products).hasSize(5)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("생선7", LocalDateTime.of(2023, 8, 17, 15, 45, 11)),
                        tuple("생선6", LocalDateTime.of(2023, 8, 16, 15, 45, 11)),
                        tuple("생선5", LocalDateTime.of(2023, 8, 15, 15, 45, 11)),
                        tuple("생선4", LocalDateTime.of(2023, 8, 14, 15, 45, 11)),
                        tuple("생선2", LocalDateTime.of(2023, 8, 12, 15, 45, 11))
                );
    }

    @Test
    @DisplayName("상품 목록 조회 시, 특정 부분만 조회한다.")
    void paging_test() {
        // given

        // when
        // 7 개 중 뒤에 3개 조회이므로, 가장 나중에 등록된 3개가 최신순으로 조회되면 됨
        List<Product> products = productQueryRepository.queryProducts(category, subCategory, RECENT, 3, 4);

        // then
        assertThat(products).hasSize(3)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("생선2", LocalDateTime.of(2023, 8, 12, 15, 45, 11)),
                        tuple("생선1", LocalDateTime.of(2023, 8, 11, 15, 45, 11)),
                        tuple("생선3", LocalDateTime.of(2023, 8, 9, 15, 45, 11))
                );
    }

    @Test
    @DisplayName("총 상품 갯수를 조회한다.")
    void total_count_test() {
        // given

        // when
        int totalCount = productQueryRepository.countByCategoryAndSubCategory(category, subCategory);

        // then
        assertThat(totalCount).isEqualTo(7);
    }

    @Test
    @DisplayName("판매자가 자신이 등록한 상품목록을 조회한다.")
    void find_all_partners_products() {
        // given

        // when
        List<ProductQueryDto> products =
                productQueryRepository.queryPartnersProducts(partners.getId(), category, subCategory, 10, 0);

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("생선7", LocalDateTime.of(2023, 8, 17, 15, 45, 11)),
                        tuple("생선6", LocalDateTime.of(2023, 8, 16, 15, 45, 11)),
                        tuple("생선5", LocalDateTime.of(2023, 8, 15, 15, 45, 11)),
                        tuple("생선4", LocalDateTime.of(2023, 8, 14, 15, 45, 11)),
                        tuple("생선2", LocalDateTime.of(2023, 8, 12, 15, 45, 11)),
                        tuple("생선1", LocalDateTime.of(2023, 8, 11, 15, 45, 11)),
                        tuple("생선3", LocalDateTime.of(2023, 8, 9, 15, 45, 11))
                );
    }

    @Test
    @DisplayName("판매자가 등록한 상품의 갯수 조회")
    void count_partners_products() {
        // given

        // when
        int count = productQueryRepository.countByPartnerIdAndCategoryAndSubCategory(partners.getId(), category, subCategory);

        // then
        assertThat(count).isEqualTo(7);
    }

    private void saveProduct(String name, int price, int quantity, double score, int salesVolume, LocalDateTime regDate) {
        String storedFileName = "storedFileName-" + name;
        String viewFileName = "viewFileName-" + name;
        String detail = name + " 상품 설명입니다.";
        productRepository.save(
                new Product(
                        name, price, quantity, score, salesVolume, regDate,
                        category, subCategory, partners.getId(),
                        storedFileName, viewFileName, detail,
                        partners.generateProductCode()
                )
        );
    }
}