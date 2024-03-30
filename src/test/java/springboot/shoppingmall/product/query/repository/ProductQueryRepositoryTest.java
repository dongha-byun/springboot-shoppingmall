package springboot.shoppingmall.product.query.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.PRICE;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.RECENT;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.SCORE;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.SELL;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;

@Transactional
@SpringBootTest
class ProductQueryRepositoryTest extends IntegrationTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductQueryRepository productQueryRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Long categoryId, subCategoryId;

    LocalDateTime now;
    Partner partners;

    @BeforeEach
    void setUp() {
        partners = partnerRepository.save(
                new Partner("테스트판매사", "테스트대표", "테스트시 테스트구 테스트동", "031-444-1234", "110-22-334411",
                        "test_partner", "test_partner1!", true)
        );
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        categoryId = category.getId();
        subCategoryId = subCategory.getId();
        now = LocalDateTime.of(2023, 8, 11, 15, 45, 11);

        saveProduct("생선3", 1500, 12, 3.0, 15, categoryId, subCategoryId, partners.getId(), now.minusDays(2));
        saveProduct("생선1", 1000, 10, 1.0, 10, categoryId, subCategoryId, partners.getId(), now);
        saveProduct("생선2", 1200, 11, 1.5, 20, categoryId, subCategoryId, partners.getId(), now.plusDays(1));
        saveProduct("생선4", 2300, 9, 2.0, 8, categoryId, subCategoryId, partners.getId(), now.plusDays(3));
        saveProduct("생선5", 9900, 1, 2.5, 11, categoryId, subCategoryId, partners.getId(), now.plusDays(4));
        saveProduct("생선6", 3900, 3, 3.5, 30, categoryId, subCategoryId, partners.getId(), now.plusDays(5));
        saveProduct("생선7", 1350, 7, 4.8, 2, categoryId, subCategoryId, partners.getId(), now.plusDays(6));
    }

    @Test
    @DisplayName("상품목록을 평점이 높은 순으로 조회한다.")
    void sort_product_by_score() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryProducts(categoryId, subCategoryId, SCORE);

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
        List<ProductQueryDto> products = productQueryRepository.queryProducts(categoryId, subCategoryId, RECENT);

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
        List<ProductQueryDto> products = productQueryRepository.queryProducts(categoryId, subCategoryId, PRICE);

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
        List<ProductQueryDto> products = productQueryRepository.queryProducts(categoryId, subCategoryId, SELL);

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
        List<ProductQueryDto> products = productQueryRepository.queryProducts(categoryId, subCategoryId, RECENT, 3,
                4);

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
        int totalCount = productQueryRepository.countByCategoryIdAndSubCategoryId(categoryId, subCategoryId);

        // then
        assertThat(totalCount).isEqualTo(7);
    }

    @Test
    @DisplayName("판매자가 자신이 등록한 상품목록을 조회한다.")
    void find_all_partners_products() {
        // given

        // when
        List<ProductQueryDto> products =
                productQueryRepository.queryPartnersProducts(partners.getId(), categoryId, subCategoryId, 10, 0);

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
    @DisplayName("판매자가 자신이 등록한 상품의 갯수를 조회한다.")
    void count_partners_products() {
        // given

        // when
        int count = productQueryRepository.countByPartnerIdAndCategoryIdAndSubCategoryId(partners.getId(), categoryId, subCategoryId);

        // then
        assertThat(count).isEqualTo(7);
    }

    @Test
    @DisplayName("판매자가 등록한 상품 목록을 조회한다.")
    void find_partners_products_all() {
        // given

        // when
        List<ProductQueryDto> products = productQueryRepository.queryPartnersProducts(
                partners.getId(), categoryId, subCategoryId, 10, 0
        );

        // then
        assertThat(products).hasSize(7)
                .extracting("name", "stock", "price")
                .containsExactly(
                        tuple("생선7", 7, 1350),
                        tuple("생선6", 3, 3900),
                        tuple("생선5", 1, 9900),
                        tuple("생선4", 9, 2300),
                        tuple("생선2", 11, 1200),
                        tuple("생선1", 10, 1000),
                        tuple("생선3", 12, 1500)
                );
    }
}