package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.dto.ProductQnaDto;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class CustomProductQnaRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductQnaRepository productQnaRepository;

    @Autowired
    CustomProductQnaRepositoryImpl customProductQnaRepository;

    @Test
    @DisplayName("상품 별 문의목록 조회")
    void find_all_product_qna_test() {
        // given
        User user1 = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        User user2 = userRepository.save(new User("사용자2", "user2", "user2!", "010-4444-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        ProductQna qna1 = productQnaRepository.save(new ProductQna("문의 드립니다. 1", product, user1.getId()));
        ProductQna qna2 = productQnaRepository.save(new ProductQna("문의 드립니다. 2", product, user2.getId()));

        // when
        List<ProductQnaDto> qnaDtos = customProductQnaRepository.findAllProductQna(product.getId());
        List<Long> ids = qnaDtos.stream().map(ProductQnaDto::getId).collect(Collectors.toList());
        List<String> contents = qnaDtos.stream().map(ProductQnaDto::getContent).collect(Collectors.toList());
        List<String> names = qnaDtos.stream().map(ProductQnaDto::getUserName).collect(Collectors.toList());

        // then
        assertThat(contents).containsExactly(
                qna2.getContent(), qna1.getContent()
        );
        assertThat(ids).containsExactly(
                qna2.getId(), qna1.getId()
        );
        assertThat(names).containsExactly(
                user2.getUserName(), user1.getUserName()
        );
    }
}