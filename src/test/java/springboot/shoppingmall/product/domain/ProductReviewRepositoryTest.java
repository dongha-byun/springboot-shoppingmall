package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class ProductReviewRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Test
    @DisplayName("내가 등록한 리뷰 목록 조회")
    void findAllReviewByUser() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product1 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));
        Product product2 = productRepository.save(new Product("상품 2", 15000, 10, category, subCategory));

        ProductReview review1 = productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product1, user.getId(), user.getLoginId()));
        ProductReview review2 = productReviewRepository.save(new ProductReview("리뷰 2 입니다.", 5, product2, user.getId(), user.getLoginId()));

        // when
        List<ProductReview> reviews = productReviewRepository.findAllByUserId(user.getId());

        // then
        assertThat(reviews).hasSize(2);
        assertThat(reviews).containsExactly(
                review1, review2
        );
    }

    @Test
    @DisplayName("사용자가 상품에 리뷰를 작성한 적이 있다.")
    void exists_user_and_product_true() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product1 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product1, user.getId(), user.getLoginId()));

        // when
        boolean isExists = productReviewRepository.existsByUserIdAndProduct(user.getId(), product1);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("사용자가 상품에 리뷰를 작성한 적이 없다.")
    void exists_user_and_product_false() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product1 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));
        Product product2 = productRepository.save(new Product("상품 2", 5000, 10, category, subCategory));

        productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product1, user.getId(), user.getLoginId()));

        // when
        boolean isExists = productReviewRepository.existsByUserIdAndProduct(user.getId(), product2);

        // then
        assertThat(isExists).isFalse();
    }
}