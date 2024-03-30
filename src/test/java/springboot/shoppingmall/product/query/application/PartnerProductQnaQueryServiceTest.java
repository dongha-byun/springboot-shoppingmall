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
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;

@Transactional
@SpringBootTest
class PartnerProductQnaQueryServiceTest extends IntegrationTest {

    @Autowired
    PartnerProductQnaQueryService service;

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
        Product product1 = saveProduct("product1", 1000, 10, 1.0, 10,
                category.getId(), subCategory.getId(), partnerId, registerDate.plusDays(1));
        Product product2 = saveProduct("product2", 1200, 11, 1.5, 20,
                category.getId(), subCategory.getId(), partnerId, registerDate.plusDays(2));
        Product product3 = saveProduct("product3", 1500, 12, 3.0, 15,
                category.getId(), subCategory.getId(), partnerId, registerDate.plusDays(3));

        productQna1 = saveQna("상품 문의 드립니다. 1", product1);
        productQna2 = saveQna("상품 문의 드립니다. 2", product2);
        productQna3 = saveQna("상품 문의 드립니다. 3", product3);
    }

    @Test
    @DisplayName("상품에 등록된 모든 문의 글들을 조회한다.")
    void find_partners_product_qna_all() {
        // given

        // when
        List<PartnersProductQnaDto> qnaList =
                service.findPartnersProductQna(partnerId, ProductQnaAnswerCompleteType.ALL);

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

}