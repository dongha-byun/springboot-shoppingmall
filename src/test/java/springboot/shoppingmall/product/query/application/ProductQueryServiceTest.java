package springboot.shoppingmall.product.query.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.*;

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
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
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

    Category category, subCategory;
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
        LocalDateTime now = LocalDateTime.of(2023, 8, 15, 12, 12, 12);
        saveProduct("슬랙스 1", 23100, 2.7, 7, now);
        saveProduct("반바지 1", 6900, 3.7, 4, now.plusDays(1));
        saveProduct("반바지 2", 6900, 3.7, 4, now.plusDays(2));
        saveProduct("반바지 3", 6900, 3.7, 4, now.plusDays(3));
        saveProduct("반바지 4", 6900, 3.7, 4, now.plusDays(4));
        saveProduct("반바지 5", 6900, 3.7, 4, now.plusDays(5));
        saveProduct("반바지 6", 6900, 3.7, 4, now.plusDays(6));
        saveProduct("반바지 7", 6900, 3.7, 4, now.plusDays(7));
        saveProduct("반바지 8", 6900, 3.7, 4, now.plusDays(8));
        saveProduct("청바지 1", 12000, 2.0, 5, now.plusDays(9));
        saveProduct("면바지 1", 17900, 1.7, 2, now.plusDays(10));
        saveProduct("면바지 2", 17900, 1.7, 2, now.plusDays(11));
        saveProduct("면바지 3", 17900, 1.7, 2, now.plusDays(12));
        saveProduct("면바지 4", 17900, 1.7, 2, now.plusDays(13));
    }

    private void saveProduct(String name, int price, double score, int salesVolume, LocalDateTime now) {
        String storedFileName = "stored-file-name-" + name;
        String viewFileName = "view-file-name-" + name;
        productRepository.save(
                new Product(
                        name, price, 10, score, salesVolume, now,
                        category, subCategory, partners.getId(),
                        storedFileName, viewFileName, "상품 설명 입니다.",
                        partners.generateProductCode()
                )
        );
    }

    @Test
    @DisplayName("상품 목록을 평점이 높은 순으로 조회한다.")
    void sort_score_products() {
        // given

        // when
        List<ProductQueryDto> scoreOrderProducts =
                productQueryService.findProductByOrder(category.getId(), subCategory.getId(), SCORE);

        // then
        assertThat(scoreOrderProducts).hasSize(14)
                .extracting("name", "score")
                .containsExactly(
                        tuple("반바지 1", 3.7),
                        tuple("반바지 2", 3.7),
                        tuple("반바지 3", 3.7),
                        tuple("반바지 4", 3.7),
                        tuple("반바지 5", 3.7),
                        tuple("반바지 6", 3.7),
                        tuple("반바지 7", 3.7),
                        tuple("반바지 8", 3.7),
                        tuple("슬랙스 1", 2.7),
                        tuple("청바지 1", 2.0),
                        tuple("면바지 1", 1.7),
                        tuple("면바지 2", 1.7),
                        tuple("면바지 3", 1.7),
                        tuple("면바지 4", 1.7)
                );
    }

    @Test
    @DisplayName("상품목록을 최신순으로 조회한다.")
    void sort_recent_products() {
        // given

        // when
        List<ProductQueryDto> recentOrderProducts =
                productQueryService.findProductByOrder(category.getId(), subCategory.getId(), RECENT);

        // then
        assertThat(recentOrderProducts).hasSize(14)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("면바지 4", LocalDateTime.of(2023, 8, 28, 12, 12, 12)),
                        tuple("면바지 3", LocalDateTime.of(2023, 8, 27, 12, 12, 12)),
                        tuple("면바지 2", LocalDateTime.of(2023, 8, 26, 12, 12, 12)),
                        tuple("면바지 1", LocalDateTime.of(2023, 8, 25, 12, 12, 12)),
                        tuple("청바지 1", LocalDateTime.of(2023, 8, 24, 12, 12, 12)),
                        tuple("반바지 8", LocalDateTime.of(2023, 8, 23, 12, 12, 12)),
                        tuple("반바지 7", LocalDateTime.of(2023, 8, 22, 12, 12, 12)),
                        tuple("반바지 6", LocalDateTime.of(2023, 8, 21, 12, 12, 12)),
                        tuple("반바지 5", LocalDateTime.of(2023, 8, 20, 12, 12, 12)),
                        tuple("반바지 4", LocalDateTime.of(2023, 8, 19, 12, 12, 12)),
                        tuple("반바지 3", LocalDateTime.of(2023, 8, 18, 12, 12, 12)),
                        tuple("반바지 2", LocalDateTime.of(2023, 8, 17, 12, 12, 12)),
                        tuple("반바지 1", LocalDateTime.of(2023, 8, 16, 12, 12, 12)),
                        tuple("슬랙스 1", LocalDateTime.of(2023, 8, 15, 12, 12, 12))
                );
    }

    @Test
    @DisplayName("상품목록을 낮은 가격 순으로 조회한다.")
    void sort_price_products() {
        // given

        // when
        List<ProductQueryDto> priceOrderProducts = productQueryService.findProductByOrder(category.getId(),
                subCategory.getId(), PRICE);

        // then
        assertThat(priceOrderProducts).hasSize(14)
                .extracting("name", "price")
                .containsExactly(
                        tuple("반바지 1", 6900),
                        tuple("반바지 2", 6900),
                        tuple("반바지 3", 6900),
                        tuple("반바지 4", 6900),
                        tuple("반바지 5", 6900),
                        tuple("반바지 6", 6900),
                        tuple("반바지 7", 6900),
                        tuple("반바지 8", 6900),
                        tuple("청바지 1", 12000),
                        tuple("면바지 1", 17900),
                        tuple("면바지 2", 17900),
                        tuple("면바지 3", 17900),
                        tuple("면바지 4", 17900),
                        tuple("슬랙스 1", 23100)
                );
    }

    @Test
    @DisplayName("상품목록을 판매량 높은 순으로 조회한다.")
    void sort_salesCount_products() {
        // given

        // when
        List<ProductQueryDto> salesOrderProducts =
                productQueryService.findProductByOrder(category.getId(), subCategory.getId(), SELL);

        // then
        assertThat(salesOrderProducts).hasSize(14)
                .extracting("name", "salesVolume")
                .containsExactly(
                        tuple("슬랙스 1", 7),
                        tuple("청바지 1", 5),
                        tuple("반바지 1", 4),
                        tuple("반바지 2", 4),
                        tuple("반바지 3", 4),
                        tuple("반바지 4", 4),
                        tuple("반바지 5", 4),
                        tuple("반바지 6", 4),
                        tuple("반바지 7", 4),
                        tuple("반바지 8", 4),
                        tuple("면바지 1", 2),
                        tuple("면바지 2", 2),
                        tuple("면바지 3", 2),
                        tuple("면바지 4", 2)
                );
    }

    @Test
    @DisplayName("검색어로 상품목록을 조회한다.")
    void search_products_keyword() {
        // given

        // when
        List<ProductQueryDto> products =
                productQueryService.searchProducts("바지", RECENT, 5, 2);

        // then
        assertThat(products).hasSize(5)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("면바지 2", LocalDateTime.of(2023, 8, 26, 12, 12, 12)),
                        tuple("면바지 1", LocalDateTime.of(2023, 8, 25, 12, 12, 12)),
                        tuple("청바지 1", LocalDateTime.of(2023, 8, 24, 12, 12, 12)),
                        tuple("반바지 8", LocalDateTime.of(2023, 8, 23, 12, 12, 12)),
                        tuple("반바지 7", LocalDateTime.of(2023, 8, 22, 12, 12, 12))
                );
    }

    @Test
    @DisplayName("상품목록을 페이징해서 조회한다.")
    void paging() {
        // given

        // when
        // 10번째 부터 10개 조회 -> 11 ~ 14 번째 상품 조회
        List<ProductQueryDto> products =
                productQueryService.findProductByOrder(category.getId(), subCategory.getId(), RECENT, 10, 10);

        // then
        assertThat(products).hasSize(4)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("반바지 3", LocalDateTime.of(2023, 8, 18, 12, 12, 12)),
                        tuple("반바지 2", LocalDateTime.of(2023, 8, 17, 12, 12, 12)),
                        tuple("반바지 1", LocalDateTime.of(2023, 8, 16, 12, 12, 12)),
                        tuple("슬랙스 1", LocalDateTime.of(2023, 8, 15, 12, 12, 12))
                );
    }

    @Test
    @DisplayName("총 상품 갯수를 조회한다.")
    void total_count() {
        // given

        // when
        int totalCount = productQueryService.getTotalCount(category.getId(), subCategory.getId());

        // then
        assertThat(totalCount).isEqualTo(14);
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void find_product() {
        // given
        Product product = productRepository.save(
                new Product(
                        "등록상품1", 23100, 10, 2.7, 7,
                        LocalDateTime.of(2023, 7, 25, 10, 22, 10),
                        category, subCategory, partners.getId(),
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        partners.generateProductCode()
                )
        );

        // when
        ProductQueryDto productDto = productQueryService.findProductOf(product.getId());

        // then
        assertThat(productDto.getId()).isEqualTo(product.getId());
        assertThat(productDto.getName()).isEqualTo("등록상품1");
        assertThat(productDto.getPartnersId()).isEqualTo(partners.getId());
        assertThat(productDto.getPartnersName()).isEqualTo("테스트판매사");
    }

    @Test
    @DisplayName("판매자가 자신이 판매하는 상품들의 목록을 조회한다.")
    void find_partners_products_all() {
        // given

        // when
        List<ProductQueryDto> products = productQueryService.findPartnersProductsAll(
                partners.getId(), category.getId(), subCategory.getId(), 10, 0
        );

        // then
        assertThat(products).hasSize(10)
                .extracting("name", "registerDate")
                .containsExactly(
                        tuple("면바지 4", LocalDateTime.of(2023, 8, 28, 12, 12, 12)),
                        tuple("면바지 3", LocalDateTime.of(2023, 8, 27, 12, 12, 12)),
                        tuple("면바지 2", LocalDateTime.of(2023, 8, 26, 12, 12, 12)),
                        tuple("면바지 1", LocalDateTime.of(2023, 8, 25, 12, 12, 12)),
                        tuple("청바지 1", LocalDateTime.of(2023, 8, 24, 12, 12, 12)),
                        tuple("반바지 8", LocalDateTime.of(2023, 8, 23, 12, 12, 12)),
                        tuple("반바지 7", LocalDateTime.of(2023, 8, 22, 12, 12, 12)),
                        tuple("반바지 6", LocalDateTime.of(2023, 8, 21, 12, 12, 12)),
                        tuple("반바지 5", LocalDateTime.of(2023, 8, 20, 12, 12, 12)),
                        tuple("반바지 4", LocalDateTime.of(2023, 8, 19, 12, 12, 12))
                );
    }

    @Test
    @DisplayName("판매자가 자신이 등록한 상품의 총 갯수를 조회한다.")
    void count_partners_products() {
        // given

        // when
        int totalCount = productQueryService.countPartnersProducts(partners.getId(), category.getId(), subCategory.getId());

        // then
        assertThat(totalCount).isEqualTo(14);
    }
}