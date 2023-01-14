package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

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
import springboot.shoppingmall.product.dto.ProductQnaRequest;
import springboot.shoppingmall.product.dto.ProductQnaResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class ProductQnaServiceTest {

    @Autowired
    ProductQnaService productQnaService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

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
    @DisplayName("상품에 대한 문의글을 등록한다.")
    void createTest(){
        // given
        ProductQnaRequest productQnaRequest = new ProductQnaRequest("제품이 이상헤요.");

        // when
        ProductQnaResponse qna = productQnaService.createQna(saveUser.getId(), product.getId(), productQnaRequest);

        // then
        assertThat(qna.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품에 대한 문의 목록을 조회한다.")
    void findQnaAllTest(){
        // given
        productQnaService.createQna(saveUser.getId(), product.getId(), new ProductQnaRequest("제품이 이상해요 1"));
        productQnaService.createQna(saveUser.getId(), product.getId(), new ProductQnaRequest("제품이 이상해요 2"));

        // when
        List<ProductQnaResponse> productQnaList = productQnaService.findQnaAllByProduct(product.getId());

        // then
        assertThat(productQnaList).hasSize(2);
        assertThat(productQnaList.get(0).getWriteDate()).isNotNull();
    }

    @Test
    @DisplayName("상품 문의 1건을 조회한다.")
    void findQnaTest(){
        // given
        ProductQnaResponse qna = productQnaService.createQna(saveUser.getId(), product.getId(),
                new ProductQnaRequest("제품에 대해 물어볼게 있어요. 1"));

        // when
        ProductQnaResponse findQna = productQnaService.findQnaByProduct(product.getId(), qna.getId());

        // then
        assertThat(findQna.getId()).isNotNull();
        assertThat(qna.getId()).isEqualTo(findQna.getId());
    }
}