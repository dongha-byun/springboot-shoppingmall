package springboot.shoppingmall.product.query.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
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
    ProductQnaRepository productQnaRepository;

    Long partnerId = 1L;
    Long writerId = 10L;

    ProductQna productQna1, productQna2, productQna3;

    @BeforeEach
    void setup() {
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime now = LocalDateTime.now();
        Product product1 = productRepository.save(
                new Product(
                        "product1", 1000, 10, 1.0, 10, now,
                        category, subCategory, partnerId,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        Product product2 = productRepository.save(
                new Product(
                        "product2", 1200, 11, 1.5, 20,
                        now.plusDays(1), category, subCategory, partnerId,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        Product product3 = productRepository.save(
                new Product(
                        "product3", 1500, 12, 3.0, 15,
                        now.plusDays(2), category, subCategory, partnerId,
                        "storedFileName3", "viewFileName3", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        productQna1 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 1", product1, writerId)
        );
        productQna2 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 2", product2, writerId)
        );
        productQna3 = productQnaRepository.save(
                new ProductQna("상품 문의 드립니다. 3", product3, writerId)
        );
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

}