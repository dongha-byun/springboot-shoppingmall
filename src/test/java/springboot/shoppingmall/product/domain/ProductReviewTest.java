package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import springboot.shoppingmall.category.domain.Category;

class ProductReviewTest {

    @Test
    @DisplayName("상품 리뷰 등록 테스트")
    void addReview() {
        // given
        Long userId = 10L;
        Product product = new Product(
                "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                new Category("상위 카테고리"), new Category("하위 카테고리"), 10L,
                "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                "test-product-code"
        );
        ProductReview productReview1 = new ProductReview("리뷰 입니다.", 4);
        ProductReview productReview2 = new ProductReview("리뷰 2 입니다.", 5);

        // when
        ProductReview review1 = productReview1.byProduct(product).byUser(userId);
        ProductReview review2 = productReview2.byProduct(product);

        // then
        assertThat(product.getReviews().getCount()).isEqualTo(2);
        assertThat(product.getReviews().getReviews()).containsExactly(
                review1, review2
        );
    }

    @Test
    @DisplayName("상품 리뷰 삭제 테스트")
    void removeReview() {
        // given
        Product product = new Product(
                "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                new Category("상위 카테고리"), new Category("하위 카테고리"), 10L,
                "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                "test-product-code"
        );
        ProductReview productReview1 = new ProductReview("리뷰 입니다.", 4);
        ProductReview productReview2 = new ProductReview("리뷰 2 입니다.", 5);

        ProductReview review1 = productReview1.byProduct(product);
        ProductReview review2 = productReview2.byProduct(product);

        // when
        product.removeReview(review1);

        // then
        assertThat(product.getReviews().getCount()).isEqualTo(1);
        assertThat(product.getReviews().getReviews()).containsExactly(
                review2
        );
    }

    @Test
    @DisplayName("상품 리뷰 목록 조회 테스트")
    void findAllReviewByProduct() {
        // given
        Long user1Id = 10L;
        Long user2Id = 20L;
        Product product = new Product(
                "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                new Category("상위 카테고리"), new Category("하위 카테고리"), 10L,
                "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                "test-product-code"
        );

        ProductReview.builder()
                .content("리뷰 입니다.")
                .score(4)
                .product(product)
                .userId(user1Id)
                .build();
        ProductReview.builder()
                .content("리뷰 2 입니다.")
                .score(5)
                .product(product)
                .userId(user2Id)
                .build();

        // when
        ProductReviews productReviews = product.getReviews();

        // then
        assertThat(productReviews.getReviews()).hasSize(2)
                .extracting("content", "score", "userId")
                .containsExactly(
                        tuple("리뷰 입니다.", 4, user1Id),
                        tuple("리뷰 2 입니다.", 5, user2Id)
                );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1:true",
            "2:false",
            "3:false"
    }, delimiterString = ":")
    @DisplayName("상품 리뷰를 작성한 사용자가 맞는지 틀린지 확인")
    void is_writer_test(Long userId, boolean result) {
        // given
        Product product = new Product(
                "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                new Category("상위 카테고리"), new Category("하위 카테고리"), 10L,
                "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                "test-product-code"
        );
        ProductReview productReview =
                ProductReview.builder()
                        .content("리뷰 등록 합니다.")
                        .score(3)
                        .product(product)
                        .userId(1L)
                        .build();

        // when
        boolean isWriter = productReview.isWriter(userId);

        // then
        assertThat(isWriter).isEqualTo(result);
    }
}