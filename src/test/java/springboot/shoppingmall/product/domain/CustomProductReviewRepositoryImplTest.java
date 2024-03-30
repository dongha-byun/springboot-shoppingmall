package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;

@Transactional
@SpringBootTest
class CustomProductReviewRepositoryImplTest extends IntegrationTest {

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    CustomProductReviewRepositoryImpl customProductReviewRepository;

    @Test
    @DisplayName("특정 상품에 작성된 리뷰 목록들을 조회한다.")
    void find_review_of_product_test() {
        // given
        Long categoryId = 1L;
        Long subCategoryId = 11L;
        Long partnersId = 10L;
        Product product =saveProduct(
                "상품 1", 12000, 20, 1.0, 10,
                categoryId, subCategoryId, partnersId, LocalDateTime.now()
        );

        Long user1Id = 10L;
        Long user2Id = 20L;
        ProductReview review1 = saveReview("리뷰 입니다.", 4, product, user1Id);
        ProductReview review2 = saveReview("리뷰 2 입니다.", 5, product, user2Id);

        // when
        List<ProductReviewDto> reviewDtos = customProductReviewRepository.findAllProductReview(product.getId());

        // then
        assertThat(reviewDtos).hasSize(2)
                .extracting("id", "content")
                .containsExactly(
                        tuple(review2.getId(), "리뷰 2 입니다."),
                        tuple(review1.getId(), "리뷰 입니다.")
                );
    }

    private ProductReview saveReview(String content, int score, Product product, Long user1Id) {
        return productReviewRepository.save(
                ProductReview.builder()
                        .content(content)
                        .score(score)
                        .product(product)
                        .userId(user1Id)
                        .build()
        );
    }

}