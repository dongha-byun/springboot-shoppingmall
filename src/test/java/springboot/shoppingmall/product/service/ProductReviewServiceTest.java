package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.domain.ProductReviewRepository;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class ProductReviewServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductReviewService service;

    @Test
    @DisplayName("상품 리뷰 등록 테스트")
    void createReviewTest() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        // when
        ProductReviewRequest productReviewRequest = new ProductReviewRequest("리뷰 등록 합니다.", 3);
        ProductUserReviewResponse response = service.createProductReview(user.getId(), product.getId(), productReviewRequest);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getContent()).isEqualTo("리뷰 등록 합니다.");
        assertThat(response.getProductName()).isEqualTo("상품 1");
    }

    @Test
    @DisplayName("사용자는 이미 리뷰를 등록한 상품에 추가로 리뷰를 등록할 수 없다.")
    void createReviewTest_fail() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        ProductReviewRequest productReviewRequest = new ProductReviewRequest("리뷰 등록 합니다.", 3);
        service.createProductReview(user.getId(), product.getId(), productReviewRequest);

        // when & then
        assertThatThrownBy(
                () -> service.createProductReview(user.getId(), product.getId(), productReviewRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 별 리뷰 목록 조회 테스트")
    void findAllReviewByProduct() {
        // given
        User user1 = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        User user2 = userRepository.save(new User("사용자2", "user2", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        ProductReview review1 = productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product, user1.getId()));
        ProductReview review2 = productReviewRepository.save(new ProductReview("리뷰 2 입니다.", 5, product, user2.getId()));

        // when
        List<ProductReviewResponse> reviews = service.findAllReview(product.getId());

        // then
        assertThat(reviews).hasSize(2);
        List<String> contents = reviews.stream()
                .map(ProductReviewResponse::getContent).collect(Collectors.toList());
        assertThat(contents).containsExactly(
                review1.getContent(), review2.getContent()
        );
    }

    @Test
    @DisplayName("사용자가 작성한 리뷰 삭제 테스트")
    void deleteProductReview() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product1 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));
        Product product2 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));

        ProductReview review1 = productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product1, user.getId()));
        ProductReview review2 = productReviewRepository.save(new ProductReview("리뷰 2 입니다.", 5, product2, user.getId()));

        // when
        service.deleteProductReview(user.getId(), review1.getId());

        // then
        List<ProductReview> reviews = productReviewRepository.findAllByUserId(user.getId());
        assertThat(reviews).hasSize(1);
        assertThat(reviews).containsExactly(review2);
        assertThat(reviews).doesNotContain(review1);
    }

    @Test
    @DisplayName("사용자가 작성한 리뷰 목록 조회 테스트")
    void findAllReviewByUser() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product1 = productRepository.save(new Product("상품 1", 12000, 20, category, subCategory));
        Product product2 = productRepository.save(new Product("상품 2", 42100, 15, category, subCategory));

        ProductReview review1 = productReviewRepository.save(new ProductReview("리뷰 입니다.", 4, product1, user.getId()));
        ProductReview review2 = productReviewRepository.save(new ProductReview("리뷰 2 입니다.", 5, product2, user.getId()));

        // when
        List<ProductUserReviewResponse> reviews = service.findAllUserReview(user.getId());

        // then
        assertThat(reviews).hasSize(2);
        List<String> contents = reviews.stream()
                .map(ProductUserReviewResponse::getContent).collect(Collectors.toList());
        assertThat(contents).containsExactly(
                review1.getContent(), review2.getContent()
        );
    }
}