package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.domain.ProductReviewRepository;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.product.service.dto.ProductReviewCreateDto;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class ProductReviewServiceTest {

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

    @Autowired
    ProductReviewService service;

    @Autowired
    OrderRepository orderRepository;

    OrderDeliveryInfo orderDeliveryInfo;

    @BeforeEach
    void beforeEach() {
        orderDeliveryInfo = new OrderDeliveryInfo(
                "test-receiver", "010-1234-1234",
                "test-zipcode", "test-address",
                "test-detail-address", "test-message"
        );
    }

    /**
     * 배송이 완료된 상품에 대해 리뷰를 남긴다.
     * 리뷰를 남긴 주문은 구매확정 처리가 되어 교환/환불이 불가하다.
     */
    @Test
    @DisplayName("상품 리뷰 등록 테스트 - 리뷰가 등록되면 구매확정 처리 한다.")
    void createReviewTest() {
        // given
        User user = saveUser("사용자1", "user1", "user1!", "010-2222-3333");
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
        Order endOrder = saveOrder("test-order-code", user, product);

        // when
        OrderItem savedOrderItem = endOrder.getItems().get(0);
        ProductReviewCreateDto createDto = new ProductReviewCreateDto("리뷰 등록 합니다.", 3);
        ProductUserReviewResponse response = service.createProductReview(
                user.getId(), savedOrderItem.getId(), product.getId(), createDto, new ArrayList<>()
        );

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getContent()).isEqualTo("리뷰 등록 합니다.");
        assertThat(response.getProductName()).isEqualTo("상품 1");

        Order finishOrder = orderRepository.findById(endOrder.getId()).orElseThrow();
        assertThat(finishOrder.getItems().get(0).getOrderStatus()).isEqualTo(OrderStatus.FINISH);

    }

    @Test
    @DisplayName("자신이 이미 리뷰를 등록한 상품에 추가로 리뷰를 등록할 수 없다.")
    void createReviewTest_fail() {
        // given
        User user = saveUser("사용자1", "user1", "user1!", "010-2222-3333");
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
        Order endOrder = saveOrder("test-order-code", user, product);

        ProductReviewCreateDto createDto = new ProductReviewCreateDto("리뷰 등록 합니다.", 3);
        OrderItem savedItem = endOrder.getItems().get(0);
        service.createProductReview(user.getId(), savedItem.getId(), product.getId(), createDto, new ArrayList<>());

        // when & then
        assertThatThrownBy(
                () -> service.createProductReview(user.getId(), savedItem.getId(), product.getId(), createDto, new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품에 등록된 리뷰 목록을 조회한다.")
    void findAllReviewByProduct() {
        // given
        User user1 = saveUser("사용자1", "user1", "user1!", "010-2222-3333");
        User user2 = saveUser("사용자2", "user2", "user1!", "010-2222-3333");
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

        ProductReview review1 = saveReview("리뷰 입니다.", 4, product, user1);
        ProductReview review2 = saveReview("리뷰 2 입니다.", 5, product, user2);

        // when
        List<ProductReviewResponse> reviews = service.findAllReview(product.getId());

        // then
        assertThat(reviews).hasSize(2)
                .extracting("content", "id")
                .containsExactly(
                        tuple("리뷰 2 입니다.", review2.getId()),
                        tuple("리뷰 입니다.", review1.getId())
                );
    }

    @Test
    @DisplayName("사용자가 자신이 작성한 리뷰를 삭제한다.")
    void deleteProductReview() {
        // given
        User user = saveUser("사용자1", "user1", "user1!", "010-2222-3333");
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
                        "상품 2", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        ProductReview review1 = saveReview("리뷰 입니다.", 4, product1, user);
        ProductReview review2 = saveReview("리뷰 2 입니다.", 5, product2, user);

        em.flush();
        em.clear();

        // when
        service.deleteProductReview(user.getId(), review1.getId());

        em.flush();
        em.clear();

        // then
        List<ProductReview> reviews = productReviewRepository.findAllByUserId(user.getId());
        assertThat(reviews).hasSize(1);

        List<Long> ids = reviews.stream()
                .map(ProductReview::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(review2.getId());
        assertThat(ids).doesNotContain(review1.getId());
    }

    @Test
    @DisplayName("사용자가 자신이 작성한 리뷰 목록을 조회한다.")
    void findAllReviewByUser() {
        // given
        User user = saveUser("사용자1", "user1", "user1!", "010-2222-3333");
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
                        "상품 2", 42100, 15, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        ProductReview review1 = saveReview("리뷰 입니다.", 4, product1, user);
        ProductReview review2 = saveReview("리뷰 2 입니다.", 5, product2, user);

        // when
        List<ProductUserReviewResponse> reviews = service.findAllUserReview(user.getId());

        // then
        assertThat(reviews).hasSize(2)
                .extracting("content", "id")
                .containsExactly(
                        tuple("리뷰 입니다.", review1.getId()),
                        tuple("리뷰 2 입니다.", review2.getId())
                );
    }

    @Test
    @DisplayName("리뷰 등록 시, 상품의 평점도 갱신된다.")
    void refresh_product_score_test() {

        // 상품 리뷰가 등록되면
        // 상품 평점이 갱신된다.

        // given
        User user1 = saveUser("사용자1", "user1", "user1!", "010-1111-3333");
        User user2 = saveUser("사용자2", "user2", "user2!", "010-2222-3333");
        User user3 = saveUser("사용자3", "user3", "user3!", "010-3333-3333");
        User user4 = saveUser("사용자4", "user4", "user4!", "010-4444-3333");
        User user5 = saveUser("사용자5", "user5", "user5!", "010-5555-3333");

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

        Order endOrder1 = saveOrder("test-order-code-1", user1, product);
        Order endOrder2 = saveOrder("test-order-code-2", user2, product);
        Order endOrder3 = saveOrder("test-order-code-3", user3, product);
        Order endOrder4 = saveOrder("test-order-code-4", user4, product);
        Order endOrder5 = saveOrder("test-order-code-5", user5, product);

        // when & then
        service.createProductReview(user1.getId(), endOrder1.getId(), product.getId(), new ProductReviewCreateDto("리뷰 남깁니다. 1", 5), new ArrayList<>());
        assertThat(product.getScore()).isEqualTo(5.0);

        service.createProductReview(user2.getId(), endOrder2.getId(), product.getId(), new ProductReviewCreateDto("리뷰 남깁니다. 2", 4), new ArrayList<>());
        assertThat(product.getScore()).isEqualTo(4.5);

        service.createProductReview(user3.getId(), endOrder3.getId(), product.getId(), new ProductReviewCreateDto("리뷰 남깁니다. 3", 2), new ArrayList<>());
        assertThat(product.getScore()).isEqualTo(3.7);

        service.createProductReview(user4.getId(), endOrder4.getId(), product.getId(), new ProductReviewCreateDto("리뷰 남깁니다. 4", 2), new ArrayList<>());
        assertThat(product.getScore()).isEqualTo(3.3);

        service.createProductReview(user5.getId(), endOrder5.getId(), product.getId(), new ProductReviewCreateDto("리뷰 남깁니다. 5", 4), new ArrayList<>());
        assertThat(product.getScore()).isEqualTo(3.4);
    }

    private Order saveOrder(String orderCode, User user, Product product) {
        List<OrderItem> items = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY_END));
        return orderRepository.save(
                new Order(orderCode, user.getId(), items, orderDeliveryInfo)
        );
    }

    private User saveUser(String name, String user1, String password, String telNo) {
        return userRepository.save(new User(name, user1, password, telNo));
    }

    private ProductReview saveReview(String content, int score, Product product, User user) {
        return productReviewRepository.save(
                ProductReview.builder()
                        .content(content)
                        .score(score)
                        .product(product)
                        .userId(user.getId())
                        .build()
        );
    }
}