package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceProductTest;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.dto.ProductReviewResponse;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.UserResponse;

public class ProductReviewAcceptanceTest extends AcceptanceProductTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    OrderResponse 배송완료_주문;
    OrderResponse 배송완료_주문2;
    OrderResponse 배송이_완료되지_않은_주문;

    @BeforeEach
    void order_acceptance_beforeEach(){
        super.acceptance_product_beforeEach();

        Product product = productRepository.findById(상품.getId()).orElseThrow();
        Product product2 = productRepository.findById(상품2.getId()).orElseThrow();
        User user = userRepository.findById(인수테스터1.getId()).orElseThrow();
        Delivery delivery = deliveryRepository.findById(배송지.getId()).orElseThrow();

        Order order = orderRepository.save(new Order(user.getId(), product, 2, delivery, OrderStatus.DELIVERY_END));
        Order order2 = orderRepository.save(new Order(user.getId(), product2, 2, delivery, OrderStatus.DELIVERY_END));
        Order deliveryOrder = orderRepository.save(new Order(user.getId(), product, 2, delivery, OrderStatus.DELIVERY));
        배송완료_주문 = OrderResponse.of(order);
        배송완료_주문2 = OrderResponse.of(order2);

        배송이_완료되지_않은_주문 = OrderResponse.of(deliveryOrder);
    }

    /**
     *  given: 사용자가 로그인 되어있음
     *  And: 배송 완료된 주문이 있음
     *  when: 리뷰를 작성하고 등록을 요청하면
     *  then: 리뷰가 등록된다.
     */
    @Test
    @DisplayName("상품 리뷰 생성")
    void create_review_test() {
        // given - before each
        // 리뷰 작성 rest 를
        // 1. order 기준으로 할지 - 내 주문 내역에서 남길 수 있다. -> 이게 좀 더 자연스러운 흐름인거 같긴하다.... -> 도메인 나누는게 애매해지는데 ㅇㅅㅇ;;
        // 2. product 기준으로 할지 - 상품을 기준으로 리뷰를 남긴다..!

        // when
        ExtractableResponse<Response> 리뷰_작성_요청_결과 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "리뷰 작성 합니다.", 4);

        // then
        assertThat(리뷰_작성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ProductUserReviewResponse 작성된_리뷰 = 리뷰_작성_요청_결과.as(ProductUserReviewResponse.class);
        assertThat(작성된_리뷰.getId()).isNotNull();
        assertThat(작성된_리뷰.getProductName()).isEqualTo(상품.getName());
        assertThat(작성된_리뷰.getContent()).isEqualTo("리뷰 작성 합니다.");
        assertThat(작성된_리뷰.getWriteDate()).isNotNull();
    }

    /**
     *  given: 사용자가 로그인 되어있음
     *  And: 배송 완료되지 않은 주문이 있음
     *  when: 리뷰를 등록을 요청하면
     *  then: 완료되지 않은 주문에 리뷰 등록이 불가하므로 리뷰가 등록되지 않는다.
     */
    @Test
    @DisplayName("배송이 완료되지 않는 주문의 리뷰는 생성할 수 없다.")
    void create_review_test_fail_by_not_end_order() {
        // given - before each

        // when
        ExtractableResponse<Response> 리뷰_작성_요청_결과 = 리뷰_작성_요청(배송이_완료되지_않은_주문, 상품, 로그인정보, "리뷰 작성 합니다.", 4);

        // then
        assertThat(리뷰_작성_요청_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     *  given: 이미 등록된 리뷰가 있음
     *  when: 리뷰를 등록을 요청하면
     *  then: 이미 작성된 리뷰가 있어서 리뷰가 등록되지 않는다.
     */
    @Test
    @DisplayName("이미 등록된 리뷰가 있으면 추가로 등록할 수 없다.")
    void create_review_test_fail_by_already_exists_review() {
        // given - before each
        리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "리뷰 작성 합니다.1", 4);

        // when
        ExtractableResponse<Response> 리뷰_작성_요청_결과 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "리뷰 작성 합니다.2", 4);

        // then
        assertThat(리뷰_작성_요청_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given: 상품에 리뷰가 등록되어 있고
     * when: 상품 리뷰의 목록을 조회하는 경우,
     * then: 상품 리뷰 목록이 조회된다.
     */
    @Test
    @DisplayName("상품 리뷰 목록 조회")
    void find_review_by_product_test() {
        // given
        String content1 = "리뷰 작성 합니다.";
        String content2 = "리뷰 작성 합니다.2";
        리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, content1, 4);
        리뷰_작성_요청(배송완료_주문, 상품, 로그인정보2, content2, 3);

        // when
        ExtractableResponse<Response> 리뷰_목록_조회_결과 = 리뷰_목록_조회_요청(상품);

        // then
        assertThat(리뷰_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(리뷰_목록_조회_결과.jsonPath().getList("content")).containsExactly(
                content2, content1
        );
        assertThat(리뷰_목록_조회_결과.jsonPath().getList("userName")).containsExactly(
                "인수테스터2", "인수테스터1"
        );
    }

    /**
     * given: 작성한 리뷰가 있음
     * when: 리뷰 삭제를 시도하면
     * then:
     */
    @Test
    @DisplayName("내가 작성한 리뷰 삭제")
    void delete_review_test() {
        // given
        ProductUserReviewResponse 작성된_리뷰 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "리뷰 작성 합니다.1", 4).as(ProductUserReviewResponse.class);

        // when
        ExtractableResponse<Response> 리뷰_삭제_결과 = 리뷰_삭제_요청(작성된_리뷰);

        // then
        assertThat(리뷰_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * given: 작성한 리뷰가 있음
     * when: 내가 작성한 리뷰 목록을 조회하려고 시도하면
     * then: 작성한 리뷰 목록들이 조회된다.
     */
    @Test
    @DisplayName("내가 작성한 리뷰 목록 조회")
    void find_reviews_test_by_user() {
        // given
        ProductUserReviewResponse 리뷰1 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "리뷰 작성 합니다.1", 4).as(
                ProductUserReviewResponse.class);
        ProductUserReviewResponse 리뷰2 = 리뷰_작성_요청(배송완료_주문2, 상품2, 로그인정보, "리뷰 작성 합니다.2", 5).as(
                ProductUserReviewResponse.class);

        // when
        ExtractableResponse<Response> 내가_작성한_리뷰_목록_조회_결과 = 내가_작성한_리뷰_목록_조회_요청();

        // then
        assertThat(내가_작성한_리뷰_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(내가_작성한_리뷰_목록_조회_결과.jsonPath().getList("id")).containsExactly(
                리뷰1.getId().intValue(), 리뷰2.getId().intValue()
        );

    }

    private ExtractableResponse<Response> 내가_작성한_리뷰_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/users/reviews")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 리뷰_삭제_요청(ProductUserReviewResponse review) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().delete("/users/reviews/{reviewId}", review.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 리뷰_작성_요청(OrderResponse order, ProductResponse product, TokenResponse token, String content, int score) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("score", score);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(token))
                .body(params)
                .when().post("/orders/{orderId}/products/{productId}/reviews", order.getId(), product.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 리뷰_목록_조회_요청(ProductResponse productResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products/{id}/reviews", productResponse.getId())
                .then().log().all()
                .extract();
    }
}
