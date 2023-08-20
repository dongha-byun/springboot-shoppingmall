package springboot.shoppingmall.product;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceProductTest;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.dto.ProductReviewRequest;
import springboot.shoppingmall.product.dto.ProductUserReviewResponse;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

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
    OrderResponse 배송완료_주문3;
    OrderResponse 배송이_완료되지_않은_주문;

    OrderDeliveryInfo orderDeliveryInfo;

    @BeforeEach
    void order_acceptance_beforeEach(){
        super.acceptance_product_beforeEach();

        Product product = productRepository.findById(상품.getId()).orElseThrow();
        Product product2 = productRepository.findById(상품2.getId()).orElseThrow();
        User user = userRepository.findById(인수테스터1.getId()).orElseThrow();
        User user2 = userRepository.findById(인수테스터2.getId()).orElseThrow();
        Delivery delivery = deliveryRepository.findById(배송지.getId()).orElseThrow();
        orderDeliveryInfo = new OrderDeliveryInfo(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage()
        );

        List<OrderItem> order1Items = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY_END));
        List<OrderItem> order2Items = List.of(new OrderItem(product2, 3, OrderStatus.DELIVERY_END));
        List<OrderItem> order3Items = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY_END));
        List<OrderItem> order4Items = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY));

        Order order = orderRepository.save(
                new Order(UUID.randomUUID().toString(), user.getId(), order1Items, orderDeliveryInfo)
        );
        Order order2 = orderRepository.save(
                new Order(UUID.randomUUID().toString(), user.getId(), order2Items, orderDeliveryInfo)
        );
        Order order3 = orderRepository.save(
                new Order(UUID.randomUUID().toString(), user2.getId(), order3Items, orderDeliveryInfo)
        );
        Order deliveryOrder = orderRepository.save(
                new Order(UUID.randomUUID().toString(), user.getId(), order4Items, orderDeliveryInfo)
        );
        배송완료_주문 = OrderResponse.of(order);
        배송완료_주문2 = OrderResponse.of(order2);
        배송완료_주문3 = OrderResponse.of(order3);

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
        assertThat(리뷰_작성_요청_결과.jsonPath().getLong("id")).isNotNull();
        assertThat(리뷰_작성_요청_결과.jsonPath().getString("productName")).isEqualTo(상품.getName());
        assertThat(리뷰_작성_요청_결과.jsonPath().getInt("score")).isEqualTo(4);
        assertThat(리뷰_작성_요청_결과.jsonPath().getString("content")).isEqualTo("리뷰 작성 합니다.");
        assertThat(리뷰_작성_요청_결과.jsonPath().getString("writeDate")).isNotNull();

        // 리뷰가 작성되면 해당 주문 건은 구매확정 처리로 변경한다.
        Order 구매확정_주문 = orderRepository.findById(배송완료_주문.getId()).orElseThrow();
        assertThat(구매확정_주문.getId()).isEqualTo(배송완료_주문.getId());
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
     *  given: 사용자가 로그인 되어있음
     *  And: 배송이 완료된 주문이 있음
     *  when: 리뷰 내용을 적지 않고 리뷰 등록을 시도하면
     *  then: 리뷰 등록에 실패한다.
     */
    @Test
    @DisplayName("리뷰 내용 없이 리뷰를 작성할 수 없다.")
    void create_review_test_fail_by_no_content() {
        // given - before each

        // when
        ExtractableResponse<Response> 리뷰_작성_요청_결과 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보, "", 4);

        // then
        assertThat(리뷰_작성_요청_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        리뷰_작성_요청(배송완료_주문2, 상품2, 로그인정보2, content2, 3);

        // when
        ExtractableResponse<Response> 리뷰_목록_조회_결과 = 리뷰_목록_조회_요청(상품);

        // then
        assertThat(리뷰_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(리뷰_목록_조회_결과.jsonPath().getList("content")).containsExactly(
                content2, content1
        );
        assertThat(리뷰_목록_조회_결과.jsonPath().getList("writerLoginId")).containsExactly(
                인수테스터2.getLoginId(), 인수테스터1.getLoginId()
        );
    }

    /**
     *  given: 상품이 등록되어 있음
     *  And: 해당 상품을 구매하고 배송 완료까지 됨
     *  And: 리뷰를 작성했음
     *  when: 상품 정보를 조회하면
     *  then: 상품 정보에 등록된 리뷰 목록도 같이 조회된다.
     */
    @Test
    @DisplayName("상품 정보 조회 - 리뷰도 같이 조회")
    void find_product_with_reviews() {
        // given
        ProductUserReviewResponse 작성리뷰1 = 리뷰_작성_요청(배송완료_주문, 상품, 로그인정보,"리뷰1 입니다.", 3).as(ProductUserReviewResponse.class);
        ProductUserReviewResponse 작성리뷰2 = 리뷰_작성_요청(배송완료_주문3,상품, 로그인정보2, "리뷰2 입니다.", 4).as(ProductUserReviewResponse.class);

        // when
        ExtractableResponse<Response> 상품_정보_조회 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/products/{id}", 상품.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(상품_정보_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(상품_정보_조회.jsonPath().getList("reviews.id", Long.class)).containsExactly(
                작성리뷰1.getId(), 작성리뷰2.getId()
        );
        assertThat(상품_정보_조회.jsonPath().getList("reviews.writerLoginId", String.class)).containsExactly(
                인수테스터1.getLoginId(), 인수테스터2.getLoginId()
        );
    }

    /**
     * given: 작성한 리뷰가 있음
     * when: 리뷰 삭제를 시도하면
     * then: 리뷰가 삭제된다.
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

    @Test
    @DisplayName("리뷰 등록 시, 최대 5장의 이미지를 첨부할 수 있다.")
    void write_review_with_image() {
        // given
        Long orderId = 배송완료_주문.getId();
        OrderItemResponse orderItem = 배송완료_주문.getItems().get(0);
        Long orderItemId = orderItem.getId();
        Long productId = orderItem.getProductId();

        MultiPartSpecification data = getDataOfMultipart("리뷰 입니다.", 3);

        // when
        ExtractableResponse<Response> 상품_리뷰_등록_결과 = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .multiPart(data)
                .multiPart("file", "image1.png", "1".getBytes(StandardCharsets.UTF_8))
                .multiPart("file", "image2.png", "2".getBytes(StandardCharsets.UTF_8))
                .multiPart("file", "image3.png", "3".getBytes(StandardCharsets.UTF_8))
                .multiPart("file", "image4.png", "4".getBytes(StandardCharsets.UTF_8))
                .multiPart("file", "image5.png", "5".getBytes(StandardCharsets.UTF_8))
                .when().post("/orders/{orderId}/{orderItemId}/products/{productId}/reviews",
                        orderId, orderItemId, productId
                )
                .then().log().all()
                .extract();

        // then
        assertThat(상품_리뷰_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(상품_리뷰_등록_결과.jsonPath().getList("images")).hasSize(5);
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

    public static ExtractableResponse<Response> 리뷰_작성_요청(
            OrderResponse order, ProductResponse product, TokenResponse token, String content, int score
    ) {
        OrderItemResponse orderItem = order.getItems().get(0);
        MultiPartSpecification data = getDataOfMultipart(content, score);

        return RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .headers(createAuthorizationHeader(token))
                .multiPart(data)
                .when().post("/orders/{orderId}/{orderItemId}/products/{productId}/reviews",
                        order.getId(),
                        orderItem.getId(),
                        product.getId()
                )
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

    private static MultiPartSpecification getDataOfMultipart(String content, int score) {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest(
                content, score
        );
        return new MultiPartSpecBuilder(productReviewRequest, ObjectMapperType.JACKSON_2)
                .controlName("data")
                .mimeType(MediaType.APPLICATION_JSON_VALUE)
                .charset("UTF-8")
                .build();
    }
}
