package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
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
import springboot.shoppingmall.product.dto.ProductQnaAnswerResponse;

@Transactional
@SpringBootTest
public class ProductQnaAnswerServiceTest {

    @Autowired
    ProductQnaAnswerService productQnaAnswerService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductQnaRepository productQnaRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    Long userId = 10L;

    @BeforeEach
    void setUp(){
        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(
                new Product(
                        "상품 1", 22000, 10, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
    }

    @Test
    @DisplayName("상품 문의의 답변을 추가한다.")
    void createTest(){
        // given
        ProductQna productQna = productQnaRepository.save(
                new ProductQna("문의글 작성합니다.", product, userId)
        );

        // when
        ProductQnaAnswerResponse qnaAnswer =
                productQnaAnswerService.createQnaAnswer(productQna.getId(), "답변 드립니다. 감사합니다");

        // then
        assertThat(qnaAnswer).isNotNull();
        assertThat(qnaAnswer.getAnswer()).contains("답변 드립니다. 감사합니다");
    }
}
