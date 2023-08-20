package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
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
    EntityManager em;

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
        Product product1 = productRepository.save(
                new Product(
                        "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        Product product2 = productRepository.save(
                new Product(
                        "상품 2", 15000, 10, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        ProductReview review1 = productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, user)
        );
        ProductReview review2 = productReviewRepository.save(
                createReview("리뷰 2 입니다.", 5, product2, user)
        );

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
        Product product1 = productRepository.save(
                new Product(
                        "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, user)
        );

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
        Product product1 = productRepository.save(
                new Product(
                        "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        Product product2 = productRepository.save(
                new Product(
                        "상품 2", 5000, 10, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, user)
        );

        // when
        boolean isExists = productReviewRepository.existsByUserIdAndProduct(user.getId(), product2);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("리뷰 저장 시, 이미지를 첨부할 수 있다.")
    void save_review_with_images() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(
                new Product(
                        "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        // when
        List<ProductReviewImage> images = Arrays.asList(
                new ProductReviewImage("stored-file-name-1", "view-file-name-1"),
                new ProductReviewImage("stored-file-name-2", "view-file-name-2"),
                new ProductReviewImage("stored-file-name-3", "view-file-name-3")
        );
        ProductReview savedReview = productReviewRepository.save(
                createReviewWithImages("리뷰 입니다.", 3, product, user, images)
        );

        em.flush();
        em.clear();

        // then
        ProductReview findReview = productReviewRepository.findById(savedReview.getId()).orElseThrow();
        assertThat(findReview.getId()).isNotNull();
        assertThat(findReview.getImages()).hasSize(3)
                .extracting("storedFileName", "viewFileName")
                .containsExactly(
                        tuple("stored-file-name-1", "view-file-name-1"),
                        tuple("stored-file-name-2", "view-file-name-2"),
                        tuple("stored-file-name-3", "view-file-name-3")
                );
    }

    private ProductReview createReview(String content, int score, Product product, User user) {
        return createReviewWithImages(content, score, product, user, new ArrayList<>());
    }

    private ProductReview createReviewWithImages(
            String content, int score, Product product, User user, List<ProductReviewImage> images
    ) {
        return ProductReview.builder()
                .content(content)
                .score(score)
                .product(product)
                .userId(user.getId())
                .writerLoginId(user.getLoginId())
                .images(images)
                .build();
    }
}