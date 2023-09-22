package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;

@Transactional
@SpringBootTest
class CustomProductReviewRepositoryImplTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    CustomProductReviewRepositoryImpl customProductReviewRepository;

    @Test
    @DisplayName("특정 상품에 작성된 리뷰 목록들을 조회한다.")
    void find_review_of_product_test() {
        // given
        Long user1Id = 10L;
        Long user2Id = 20L;
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