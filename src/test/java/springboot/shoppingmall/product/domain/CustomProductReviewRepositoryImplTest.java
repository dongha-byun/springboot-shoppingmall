package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.dto.ProductReviewDto;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class CustomProductReviewRepositoryImplTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    CustomProductReviewRepositoryImpl customProductReviewRepository;

    @Test
    @DisplayName("상품의 리뷰 목록 조회 - dto 로 조회")
    void find_review_of_product_test() {
        // given
        User user1 = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        User user2 = userRepository.save(new User("사용자2", "user2", "user2!", "010-4444-3333"));
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

        ProductReview review1 = productReviewRepository.save(
                ProductReview.builder()
                        .content("리뷰 입니다.")
                        .score(4)
                        .product(product)
                        .userId(user1.getId())
                        .writerLoginId(user1.getLoginId())
                        .build()
        );
        ProductReview review2 = productReviewRepository.save(
                ProductReview.builder()
                        .content("리뷰 2 입니다.")
                        .score(5)
                        .product(product)
                        .userId(user2.getId())
                        .writerLoginId(user2.getLoginId())
                        .build()
        );

        // when
        List<ProductReviewDto> reviewDtos = customProductReviewRepository.findAllProductReview(product.getId());

        // then
        assertThat(reviewDtos).hasSize(2)
                .extracting("id", "content", "writerLoginId")
                .containsExactly(
                        tuple(review2.getId(), "리뷰 2 입니다.", "user2"),
                        tuple(review1.getId(), "리뷰 입니다.", "user1")
                );
    }

}