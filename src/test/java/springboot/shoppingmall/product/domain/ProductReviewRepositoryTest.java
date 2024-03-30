package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;

@Transactional
@SpringBootTest
class ProductReviewRepositoryTest extends IntegrationTest {

    @Autowired
    ProductReviewRepository productReviewRepository;

    Product product1, product2;

    @BeforeEach
    void setup() {
        Long category = 1L;
        Long subCategory = 11L;
        product1 = saveProduct(
                "상품 1", 12000, 20, 1.0, 10,
                category, subCategory, 10L, LocalDateTime.now()
        );
        product2 = saveProduct(
                "상품 2", 15000, 10, 1.0, 10,
                category, subCategory, 10L, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("내가 등록한 리뷰 목록 조회")
    void findAllReviewByUser() {
        // given
        Long userId = 10L;
        ProductReview review1 = productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, userId)
        );
        ProductReview review2 = productReviewRepository.save(
                createReview("리뷰 2 입니다.", 5, product2, userId)
        );

        // when
        List<ProductReview> reviews = productReviewRepository.findAllByUserId(userId);

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
        Long userId = 10L;
        productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, userId)
        );

        // when
        boolean isExists = productReviewRepository.existsByUserIdAndProduct(userId, product1);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("사용자가 상품에 리뷰를 작성한 적이 없다.")
    void exists_user_and_product_false() {
        // given
        Long userId = 10L;
        productReviewRepository.save(
                createReview("리뷰 입니다.", 4, product1, userId)
        );

        // when
        boolean isExists = productReviewRepository.existsByUserIdAndProduct(userId, product2);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("리뷰 저장 시, 이미지를 첨부할 수 있다.")
    void save_review_with_images() {
        // given
        Long userId = 10L;

        // when
        List<ProductReviewImage> images = Arrays.asList(
                new ProductReviewImage("stored-file-name-1", "view-file-name-1"),
                new ProductReviewImage("stored-file-name-2", "view-file-name-2"),
                new ProductReviewImage("stored-file-name-3", "view-file-name-3")
        );
        ProductReview savedReview = productReviewRepository.save(
                createReviewWithImages("리뷰 입니다.", 3, product1, userId, images)
        );

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

    private ProductReview createReview(String content, int score, Product product, Long userId) {
        return createReviewWithImages(content, score, product, userId, new ArrayList<>());
    }

    private ProductReview createReviewWithImages(
            String content, int score, Product product, Long userId, List<ProductReviewImage> images
    ) {
        return ProductReview.builder()
                .content(content)
                .score(score)
                .product(product)
                .userId(userId)
                .images(images)
                .build();
    }
}