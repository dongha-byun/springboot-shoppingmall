package springboot.shoppingmall.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.product.application.dto.ProductQnaCreateDto;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.presentation.response.ProductQnaResponse;

@Transactional
@SpringBootTest
class ProductQnaServiceTest extends IntegrationTest {

    @Autowired
    ProductQnaService productQnaService;

    Product product;
    Long userId = 10L;

    @BeforeEach
    void setUp(){
        Long categoryId = 1L;
        Long subCategoryId = 11L;
        Long partnersId = 1L;
        product = saveProduct(
                "상품 1", 22000, 10, 1.0, 10,
                categoryId, subCategoryId, partnersId, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("상품에 대한 문의글을 등록한다.")
    void createTest(){
        // given
        ProductQnaCreateDto productQnaCreateDto = new ProductQnaCreateDto("제품이 이상헤요.");

        // when
        ProductQnaDto qna = productQnaService.createQna(userId, product.getId(), productQnaCreateDto);

        // then
        assertThat(qna.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품에 대한 문의 목록을 조회한다.")
    void findQnaAllTest(){
        // given
        productQnaService.createQna(userId, product.getId(), new ProductQnaCreateDto("제품이 이상해요 1"));
        productQnaService.createQna(userId, product.getId(), new ProductQnaCreateDto("제품이 이상해요 2"));

        // when
        List<ProductQnaDto> qnaDtos = productQnaService.findQnaAllByProduct(product.getId());

        // then
        assertThat(qnaDtos).hasSize(2);
        assertThat(qnaDtos.get(0).getWriteDate()).isNotNull();
    }

    @Test
    @DisplayName("상품 문의 1건을 조회한다.")
    void findQnaTest(){
        // given
        ProductQnaCreateDto createDto = new ProductQnaCreateDto("제품에 대해 물어볼게 있어요. 1");
        ProductQnaDto qna = productQnaService.createQna(userId, product.getId(), createDto);

        // when
        ProductQnaResponse findQna = productQnaService.findQnaByProduct(product.getId(), qna.getId());

        // then
        assertThat(findQna.getId()).isNotNull();
        assertThat(qna.getId()).isEqualTo(findQna.getId());
    }
}