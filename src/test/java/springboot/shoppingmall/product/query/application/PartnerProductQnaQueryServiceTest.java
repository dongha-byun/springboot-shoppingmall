package springboot.shoppingmall.product.query.application;

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
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;

@Transactional
@SpringBootTest
class PartnerProductQnaQueryServiceTest {

    @Autowired
    PartnerProductQnaQueryService service;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductQnaRepository qnaRepository;

    @Autowired
    ProductQnaAnswerRepository qnaAnswerRepository;

    Long partnerId = 1L;
    Long writerId = 10L;

    ProductQna productQna1, productQna2, productQna3;

    @BeforeEach
    void setup() {
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime registerDate = LocalDateTime.of(2023, 8, 12, 0, 0, 0);
        Product product1 = saveProduct("product1", 1000, 10, 10, 1.0,
                registerDate.plusDays(1), category, subCategory, "storedFileName1", "viewFileName1");
        Product product2 = saveProduct("product2", 1200, 11, 20, 1.5,
                registerDate.plusDays(2), category, subCategory, "storedFileName2", "viewFileName2");
        Product product3 = saveProduct("product3", 1500, 12, 15, 3.0,
                registerDate.plusDays(3), category, subCategory, "storedFileName3", "viewFileName3");

        productQna1 = saveQna("상품 문의 드립니다. 1", product1);
        productQna2 = saveQna("상품 문의 드립니다. 2", product2);
        productQna3 = saveQna("상품 문의 드립니다. 3", product3);
    }

    @Test
    @DisplayName("상품에 등록된 모든 문의 글들을 조회한다.")
    void find_partners_product_qna_all() {
        // given

        // when
        List<PartnersProductQnaDto> qnaList = service.findPartnersProductQna(partnerId,
                ProductQnaAnswerCompleteType.ALL);

        // then
        assertThat(qnaList).hasSize(3)
                .extracting("content", "productName")
                .containsExactly(
                        tuple("상품 문의 드립니다. 1", "product1"),
                        tuple("상품 문의 드립니다. 2", "product2"),
                        tuple("상품 문의 드립니다. 3", "product3")
                );
    }

    @Test
    @DisplayName("판매자가 상품에 등록된 문의 글 중 답변을 달지 않은 글들을 조회한다.")
    void find_partners_product_qna_no_answer() {
        // given
        saveQnaAnswer("답변입니다.", productQna1);

        // when
        List<PartnersProductQnaDto> qnaList =
                service.findPartnersProductQna(partnerId, ProductQnaAnswerCompleteType.N);

        // then
        assertThat(qnaList).hasSize(2)
                .extracting("content", "productName", "isAnswered")
                .containsExactly(
                        tuple("상품 문의 드립니다. 2", "product2", false),
                        tuple("상품 문의 드립니다. 3", "product3", false)
                );
    }

    @Test
    @DisplayName("판매자가 상품에 등록된 문의 글 중 답변이 존재하는 문의들을 조회한다.")
    void find_partners_product_qna_has_answer() {
        // given
        saveQnaAnswer("답변 1 입니다.", productQna1);
        saveQnaAnswer("답변 3 입니다.", productQna3);

        // when
        List<PartnersProductQnaDto> qnaList =
                service.findPartnersProductQna(partnerId, ProductQnaAnswerCompleteType.Y);

        // then
        assertThat(qnaList).hasSize(2)
                .extracting("content", "productName", "isAnswered")
                .containsExactly(
                        tuple("상품 문의 드립니다. 1", "product1", true),
                        tuple("상품 문의 드립니다. 3", "product3", true)
                );
    }

    private void saveQnaAnswer(String content, ProductQna qna) {
        qnaAnswerRepository.save(
                ProductQnaAnswer.createQnaAnswer(content, qna)
        );
    }

    private ProductQna saveQna(String content, Product product1) {
        return qnaRepository.save(
                new ProductQna(content, product1, writerId)
        );
    }

    private Product saveProduct(String product1, int price, int count, int count1, double score, LocalDateTime now,
                                Category category, Category subCategory, String storedFileName1, String viewFileName1) {
        return productRepository.save(
                new Product(
                        product1, price, count, score, count1, now,
                        category, subCategory, partnerId,
                        storedFileName1, viewFileName1, "상품 설명 입니다.",
                        "test-product-code"
                )
        );
    }
}