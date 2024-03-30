package springboot.shoppingmall.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaRepository;

@Transactional
@SpringBootTest
public class ProductQnaAnswerServiceTest extends IntegrationTest {

    @Autowired
    ProductQnaAnswerService productQnaAnswerService;

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
        product = saveProduct(
                "상품 1", 22000, 10, 1.0, 10,
                category.getId(), subCategory.getId(), 10L, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("상품 문의의 답변을 추가한다.")
    void create(){
        // given
        ProductQna productQna = productQnaRepository.save(
                new ProductQna("문의글 작성합니다.", product, userId)
        );

        // when
        ProductQnaAnswerDto dto =
                productQnaAnswerService.createQnaAnswer(productQna.getId(), "답변 드립니다. 감사합니다");

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getAnswer()).contains("답변 드립니다. 감사합니다");
    }

    @Test
    @DisplayName("존재하지 않는 답변에 문의를 등록할 수 없다.")
    void create_fail_with_no_qna() {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productQnaAnswerService.createQnaAnswer(999L, "답변 드립니다. 감사합니다.")
        );
    }

}
