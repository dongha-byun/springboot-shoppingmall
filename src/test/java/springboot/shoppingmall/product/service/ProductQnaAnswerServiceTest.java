package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

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
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
public class ProductQnaAnswerServiceTest {

    @Autowired
    ProductQnaAnswerService productQnaAnswerService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductQnaRepository productQnaRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    User saveUser;

    @BeforeEach
    void setUp(){
        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(new Product("상품 1", 22000, 10, category, subCategory));
        saveUser = userRepository.save(User.builder()
                .userName("테스터1")
                .loginId("tester1")
                .password("tester1!")
                .telNo("010-2222-3333")
                .build());
    }

    @Test
    @DisplayName("상품 문의의 답변을 추가한다.")
    void createTest(){
        // given
        ProductQna productQna = productQnaRepository.save(new ProductQna("문의글 작성합니다.", product, saveUser.getId()));

        // when
        ProductQnaAnswerResponse qnaAnswer = productQnaAnswerService.createQnaAnswer(productQna.getId(), "답변 드립니다. 감사합니다");

        // then
        assertThat(qnaAnswer).isNotNull();
        assertThat(qnaAnswer.getAnswer()).contains("답변 드립니다. 감사합니다");
    }
}
