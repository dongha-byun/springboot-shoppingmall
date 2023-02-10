package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.user.domain.User;

class ProductReviewTest {

    @Test
    @DisplayName("상품 리뷰 등록 테스트")
    void addReview() {
        // given
        User user = new User("사용자1", "user1", "user1!", "010-2222-3333");
        Product product = new Product("상품 1", 12000, 20, new Category("상위 카테고리"), new Category("하위 카테고리"));
        ProductReview productReview1 = new ProductReview("리뷰 입니다.", 4);
        ProductReview productReview2 = new ProductReview("리뷰 2 입니다.", 5);

        // when
        ProductReview review1 = productReview1.byProduct(product).byUser(user);
        ProductReview review2 = productReview2.byProduct(product);

        // then
        assertThat(product.getReviews()).hasSize(2);
        assertThat(product.getReviews()).containsExactly(
                review1, review2
        );
    }

    @Test
    @DisplayName("상품 리뷰 삭제 테스트")
    void removeReview() {
        // given
        Product product = new Product("상품 1", 12000, 20, new Category("상위 카테고리"), new Category("하위 카테고리"));
        ProductReview productReview1 = new ProductReview("리뷰 입니다.", 4);
        ProductReview productReview2 = new ProductReview("리뷰 2 입니다.", 5);

        ProductReview review1 = productReview1.byProduct(product);
        ProductReview review2 = productReview2.byProduct(product);

        // when
        product.removeReview(review1);

        // then
        assertThat(product.getReviews()).hasSize(1);
        assertThat(product.getReviews()).containsExactly(
                review2
        );
    }
}