package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductReviewsTest {

    @Test
    @DisplayName("상품 리뷰 추가 테스트")
    void add_review_test() {
        // given
        ProductReviews reviews = new ProductReviews();

        // when
        ProductReview review = new ProductReview("리뷰 등록 합니다.", 3);
        reviews.addReview(review);

        // then
        assertThat(reviews.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 리뷰 제거 테스트")
    void remove_review_test() {
        // given
        ProductReviews reviews = new ProductReviews();
        ProductReview review1 = new ProductReview("리뷰 등록 합니다. 1", 3);
        ProductReview review2 = new ProductReview("리뷰 등록 합니다. 2", 4);

        reviews.addReview(review1);
        reviews.addReview(review2);

        // when
        reviews.removeReview(review1);

        // then
        assertThat(reviews.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰들의 평균 평점 조회")
    void get_average_score_test() {
        // given
        ProductReviews reviews = new ProductReviews();
        reviews.addReview(new ProductReview("리뷰 입니다.", 3));
        reviews.addReview(new ProductReview("리뷰 입니다.", 4));
        reviews.addReview(new ProductReview("리뷰 입니다.", 4));

        // when
        double averageScore = reviews.getAverageScore();

        // then
        assertThat(averageScore).isEqualTo(3.7);
    }
}