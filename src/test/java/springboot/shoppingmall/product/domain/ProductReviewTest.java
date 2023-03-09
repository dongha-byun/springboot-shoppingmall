package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
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
        ProductReview review1 = productReview1.byProduct(product).byUser(user.getId());
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
        Product product = new Product("상품 1", 12000, 20, new Category("상위 카테고리"), new Category("하위 카테고리"));
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
        User user1 = new User("사용자1", "user1", "user1!", "010-2222-3333");
        User user2 = new User("사용자2", "user2", "user2@", "010-4444-5555");
        Product product = new Product("상품 1", 12000, 20, new Category("상위 카테고리"), new Category("하위 카테고리"));
        ProductReview productReview1 = new ProductReview("리뷰 입니다.", 4, product, user1.getId());
        ProductReview productReview2 = new ProductReview("리뷰 2 입니다.", 5, product, user2.getId());

        // when
        List<ProductReview> reviews = product.getReviews().getReviews();

        // then
        assertThat(reviews).hasSize(2);

        List<String> contents = reviews.stream()
                .map(ProductReview::getContent).collect(Collectors.toList());
        assertThat(contents).containsExactly(
                productReview1.getContent(), productReview2.getContent()
        );

        List<Integer> scores = reviews.stream()
                .map(ProductReview::getScore).collect(Collectors.toList());
        assertThat(scores).containsExactly(
                productReview1.getScore(), productReview2.getScore()
        );

        List<Long> users = reviews.stream()
                .map(ProductReview::getUserId).collect(Collectors.toList());
        assertThat(users).containsExactly(
                user1.getId(), user2.getId()
        );
    }
}