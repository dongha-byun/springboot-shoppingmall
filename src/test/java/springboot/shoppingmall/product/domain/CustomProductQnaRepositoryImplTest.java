package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
import springboot.shoppingmall.product.application.dto.ProductQnaDto;

@Transactional
@SpringBootTest
class CustomProductQnaRepositoryImplTest extends IntegrationTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductQnaRepository productQnaRepository;

    @Autowired
    CustomProductQnaRepositoryImpl customProductQnaRepository;

    Product product;

    @BeforeEach
    void setup() {
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        product = saveProduct(
                "상품 1", 12000, 20, 1.0, 10,
                category.getId(), subCategory.getId(), 10L, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("특정 상품에 등록된 문의 목록을 조회한다.")
    void find_all_product_qna() {
        // given
        Long user1Id = 10L;
        Long user2Id = 20L;

        ProductQna qna1 = saveProductQna("문의 드립니다. 1", product, user1Id);
        ProductQna qna2 = saveProductQna("문의 드립니다. 2", product, user2Id);

        // when
        List<ProductQnaDto> qnaDtos = customProductQnaRepository.findAllProductQna(product.getId());

        // then
        assertThat(qnaDtos).hasSize(2)
                .extracting("content", "id")
                .containsExactly(
                        tuple("문의 드립니다. 2", qna2.getId()),
                        tuple("문의 드립니다. 1", qna1.getId())
                );
    }

    private ProductQna saveProductQna(String content, Product product, Long user1Id) {
        return productQnaRepository.save(new ProductQna(content, product, user1Id));
    }
}