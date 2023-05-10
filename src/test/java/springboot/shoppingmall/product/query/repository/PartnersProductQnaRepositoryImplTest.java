package springboot.shoppingmall.product.query.repository;

import static org.assertj.core.api.Assertions.*;

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
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class PartnersProductQnaRepositoryImplTest {

    @Autowired
    PartnersProductQnaRepositoryImpl partnersProductQnaRepository;

    @Autowired
    ProductQnaRepository productQnaRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductQnaAnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;
    Long partnerId = 1L;
    User writer;
    Product product1;
    Product product2;
    Product product3;

    @BeforeEach
    void setup() {
        writer = userRepository.save(
                new User("테스트문의작성자", "testQnaWriter", "testQnaWriter1!", "010-2222-3333")
        );
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime now = LocalDateTime.now();
        product1 = productRepository.save(
                new Product(
                        "product1", 1000, 10, 1.0, 10, now,
                        category, subCategory, partnerId,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다."
                )
        );
        product2 = productRepository.save(
                new Product(
                        "product2", 1200, 11, 1.5, 20,
                        now.plusDays(1), category, subCategory, partnerId,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다."
                )
        );
        product3 = productRepository.save(
                new Product(
                        "product3", 1500, 12, 3.0, 15,
                        now.plusDays(2), category, subCategory, partnerId,
                        "storedFileName3", "viewFileName3", "상품 설명 입니다."
                )
        );
    }

    @Test
    @DisplayName("판매자가 등록한 상품에 등록된 답변 미등록 문의글 목록 조회")
    void find_partners_product_qna_no_answer() {
        // given
        ProductQna qna1 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 1", product1, writer.getId(), writer.getLoginId())
        );
        ProductQna qna2 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 2", product2, writer.getId(), writer.getLoginId())
        );
        ProductQna qna3 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 3", product3, writer.getId(), writer.getLoginId())
        );

        // when
        List<PartnersProductQnaDto> qnas = partnersProductQnaRepository.findPartnersProductQnaAll(partnerId,
                ProductQnaAnswerCompleteType.N);

        // then
        assertThat(qnas).hasSize(3);

        List<Long> ids = qnas.stream()
                .map(PartnersProductQnaDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                qna1.getId(), qna2.getId(), qna3.getId()
        );

        List<String> writerLoginIds = qnas.stream()
                .map(PartnersProductQnaDto::getWriterLoginId)
                .collect(Collectors.toList());
        assertThat(writerLoginIds).containsExactly(
                writer.getLoginId(), writer.getLoginId(), writer.getLoginId()
        );

        List<Boolean> isAnsweredList = qnas.stream()
                .map(PartnersProductQnaDto::isAnswered)
                .collect(Collectors.toList());
        assertThat(isAnsweredList).containsExactly(
                false, false, false
        );

        List<String> productNames = qnas.stream()
                .map(PartnersProductQnaDto::getProductName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(
                product1.getName(), product2.getName(), product3.getName()
        );

        List<String> imgFileNames = qnas.stream()
                .map(PartnersProductQnaDto::getImgFileName)
                .collect(Collectors.toList());
        assertThat(imgFileNames).containsExactly(
                product1.getThumbnail(), product2.getThumbnail(), product3.getThumbnail()
        );
    }

    @Test
    @DisplayName("판매자가 등록한 상품에 등록된 답변 등록된 문의글 목록 조회")
    void find_partners_product_qna_has_answer() {
        // given
        ProductQna qna1 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 1", product1, writer.getId(), writer.getLoginId())
        );
        ProductQna qna2 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 2", product2, writer.getId(), writer.getLoginId())
        );
        ProductQna qna3 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 3", product3, writer.getId(), writer.getLoginId())
        );

        ProductQnaAnswer answerOfQna1 = answerRepository.save(
                ProductQnaAnswer.createQnaAnswer("문의 1 에 대한 답변 드립니다.", qna1)
        );
        ProductQnaAnswer answerOfQna3 = answerRepository.save(
                ProductQnaAnswer.createQnaAnswer("문의 3 에 대한 답변 드립니다.", qna3)
        );

        // when
        List<PartnersProductQnaDto> qnas = partnersProductQnaRepository.findPartnersProductQnaAll(
                partnerId, ProductQnaAnswerCompleteType.Y);

        // then
        assertThat(qnas).hasSize(2);

        List<Long> ids = qnas.stream()
                .map(PartnersProductQnaDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                qna1.getId(), qna3.getId()
        );

        List<Boolean> isAnsweredList = qnas.stream()
                .map(PartnersProductQnaDto::isAnswered)
                .collect(Collectors.toList());
        assertThat(isAnsweredList).containsExactly(
                true, true
        );

        List<String> productNames = qnas.stream()
                .map(PartnersProductQnaDto::getProductName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(
                product1.getName(), product3.getName()
        );

        List<String> imgFileNames = qnas.stream()
                .map(PartnersProductQnaDto::getImgFileName)
                .collect(Collectors.toList());
        assertThat(imgFileNames).containsExactly(
                product1.getThumbnail(), product3.getThumbnail()
        );
    }

    @Test
    @DisplayName("판매자가 등록한 상품에 등록된 모든 문의글 목록 조회")
    void find_partners_product_qna_all() {
        // given
        ProductQna qna1 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 1", product1, writer.getId(), writer.getLoginId())
        );
        ProductQna qna2 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 2", product2, writer.getId(), writer.getLoginId())
        );
        ProductQna qna3 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 3", product3, writer.getId(), writer.getLoginId())
        );

        ProductQnaAnswer answerOfQna1 = answerRepository.save(
                ProductQnaAnswer.createQnaAnswer("문의 1 에 대한 답변 드립니다.", qna1)
        );
        ProductQnaAnswer answerOfQna3 = answerRepository.save(
                ProductQnaAnswer.createQnaAnswer("문의 3 에 대한 답변 드립니다.", qna3)
        );

        // when
        List<PartnersProductQnaDto> qnas = partnersProductQnaRepository.findPartnersProductQnaAll(
                partnerId, ProductQnaAnswerCompleteType.ALL);

        // then
        assertThat(qnas).hasSize(3);

        List<Long> ids = qnas.stream()
                .map(PartnersProductQnaDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                qna1.getId(), qna2.getId(), qna3.getId()
        );

        List<Boolean> isAnsweredList = qnas.stream()
                .map(PartnersProductQnaDto::isAnswered)
                .collect(Collectors.toList());
        assertThat(isAnsweredList).containsExactly(
                true, false, true
        );

        List<String> productNames = qnas.stream()
                .map(PartnersProductQnaDto::getProductName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(
                product1.getName(), product2.getName(), product3.getName()
        );

        List<String> imgFileNames = qnas.stream()
                .map(PartnersProductQnaDto::getImgFileName)
                .collect(Collectors.toList());
        assertThat(imgFileNames).containsExactly(
                product1.getThumbnail(), product2.getThumbnail(), product3.getThumbnail()
        );
    }
}