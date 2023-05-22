package springboot.shoppingmall.order;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springboot.shoppingmall.AcceptanceProductTest;
import springboot.shoppingmall.authorization.exception.ErrorCode;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.DeliveryEndRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.PayType;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.DeliveryResponse;
public class OrderAcceptanceTest extends AcceptanceProductTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    OrderResponse 배송완료_주문;

    @BeforeEach
    void order_acceptance_beforeEach(){
        super.acceptance_product_beforeEach();

        Product product1 = productRepository.findById(상품.getId()).orElseThrow();
        Product product2 = productRepository.findById(상품2.getId()).orElseThrow();

        User user = userRepository.findById(인수테스터1.getId()).orElseThrow();
        Delivery delivery = deliveryRepository.findById(배송지.getId()).orElseThrow();
        List<OrderItem> orderItems = Arrays.asList(
                new OrderItem(product1, 2, OrderStatus.READY),
                new OrderItem(product2, 3, OrderStatus.READY)
        );
        Order order = orderRepository.save(
                new Order("test-order-code", user.getId(), orderItems, OrderStatus.DELIVERY_END,
                        delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(),
                        delivery.getDetailAddress(), delivery.getRequestMessage())
        );

        배송완료_주문 = OrderResponse.of(order);
    }

    /**
     * Feature: 최초 주문
     *  Background:
     *      given: 로그인한 사용자
     *      And: 등록되어있는 상품
     *      And: 사용자가 등록해놓은 배송지 정보가 존재함
     *
     *  Scenario: 최초 주문 생성
     *      when: 구매하고자 하는 상품정보와  갯수, 배송지 정보를 가지고 주문을 생성하면
     *      then: 주문 내역 조회 시, 주문된 내용이 조회된다.
     */
    @Test
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        // given

        // when
        ExtractableResponse<Response> 주문_생성_결과 = 주문_생성_요청(상품, 3, 3000, 배송지);

        // then
        assertThat(주문_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(주문_생성_결과.jsonPath().getLong("id")).isNotNull();
        assertThat(주문_생성_결과.jsonPath().getString("orderDate")).isNotNull();
        assertThat(주문_생성_결과.jsonPath().getString("orderCode")).isNotNull();
        assertThat(주문_생성_결과.jsonPath().getString("orderStatusName")).isEqualTo(OrderStatus.READY.getStatusName());
        assertThat(주문_생성_결과.jsonPath().getString("receiverName")).isEqualTo(배송지.getReceiverName());
    }

    /**
     * Feature: 최초 주문 (여러 상품)
     *  Background:
     *      given: 로그인한 사용자
     *      And: 등록되어있는 상품들
     *      And: 사용자가 등록해놓은 배송지 정보가 존재함
     *
     *  Scenario: 최초 주문 생성
     *      when: 구매하고자 하는 상품정보와  갯수, 배송지 정보를 가지고 주문을 생성하면
     *      then: 주문 내역 조회 시, 주문된 내용이 조회된다.
     */
    @Test
    @DisplayName("여러 상품 주문 테스트")
    void order_with_many_products() {
        // given

        // when


        // then
    }

    /**
     * Feature: 준비중인 주문의 상태를 출고중으로 변경
     *  Background:
     *      given: 판매자 정보로 로그인 되어있음
     *      And: 판매자가 판매한 상품의 준비중인 주문이 들어와 있음
     *  Scenario: 준비중인 주문의 상태을 출고중으로 변경
     *      when: 조회된 주문정보의 주문상태를 출고중으로 변경하면
     *      then: 주문정보가 출고중으로 변경된다.
     */
    @Test
    @DisplayName("주문 상태 변경 : 준비중 -> 출고중")
    void statusChangeTest1(){
        // given
        OrderResponse 주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_출고중_요청(주문);

        // then
        assertThat(주문_상태_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(주문_상태_변경_결과.jsonPath().getList("items.invoiceNumber", String.class)).containsExactly(
                주문.getItems().get(0).getInvoiceNumber()
        );
        assertThat(주문_상태_변경_결과.jsonPath().getList("items.orderStatusName", String.class)).containsExactly(
                OrderStatus.OUTING.getStatusName()
        );
    }

    /**
     * Feature: 주문 배송 완료 테스트
     *  Background:
     *      given: 판매자 정보로 로그인 되어있음
     *      And: 판매자가 판매한 상품의 준비중인 주문이 들어와 있음
     *  Scenario: 주문 상태 변경 시나리오
     *      when: 준비 중인 주문을 출고중으로 변경하면
     *      then: 주문 상태가 출고중으로 변경되고,
     *      when: 출고중인 주문 상태를 주문 취소로 변경하면,
     *      then: 준비 중인 주문이 아니므로, 주문 취소 상태로 변경되지 않고,
     *      when: 출고중인 주문 상태를 배송 중으로 변경하면,
     *      then: 주문 상태가 배송중으로 변경되고
     *      when: 배송이 완료되어 배송완료 처리를 하면
     *      then: 주문 상태가 배송완료로 변경된다.
     */
    @Test
    @DisplayName("주문 배송 완료 테스트")
    void 배송_완료_테스트() {
        // given
        OrderResponse 준비중_주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);
        assertThat(준비중_주문.getOrderStatusName()).isEqualTo(OrderStatus.READY.getStatusName());

        // when : 준비 중인 주문을 출고중으로 변경하면
        ExtractableResponse<Response> 준비중_에서_출고중_변경_결과 = 주문_출고중_요청(준비중_주문);
        assertThat(준비중_에서_출고중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 출고중으로 변경되고
        OrderResponse 출고중_주문 = 준비중_에서_출고중_변경_결과.as(OrderResponse.class);
        assertThat(출고중_주문.getOrderStatusName()).isEqualTo(OrderStatus.OUTING.getStatusName());

        // when : 출고중인 주문 상태를 주문 취소로 변경하면
        String cancelReason = "주문 취소 합니다.";
        ExtractableResponse<Response> 출고중_에서_주문취소_변경_결과 = 주문_주문취소_요청(출고중_주문, cancelReason);

        // then : 준비 중인 주문이 아니므로, 주문 취소 상태로 변경되지 않고
        assertThat(출고중_에서_주문취소_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // when : 출고중인 주문 상태를 배송 중으로 변경하면
        String 출고중_송장번호 = 출고중_주문.getItems().get(0).getInvoiceNumber();
        ExtractableResponse<Response> 출고중_에서_배송중_변경_결과 = 주문_배송중_요청(출고중_송장번호);
        assertThat(출고중_에서_배송중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 배송중으로 변경되고
        OrderResponse 배송중_주문 = 출고중_에서_배송중_변경_결과.as(OrderResponse.class);
        assertThat(배송중_주문.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());

        // when: 배송이 완료되어 배송완료 처리를 하면
        LocalDateTime deliveryDate = LocalDateTime.of(2023, 5, 5, 15, 30, 30);
        String deliveryPlace = "무인택배함";
        DeliveryEndRequest deliveryEndRequest = new DeliveryEndRequest(deliveryDate, deliveryPlace);

        String 배송중_송장번호 = 배송중_주문.getItems().get(0).getInvoiceNumber();
        ExtractableResponse<Response> 배송중_에서_배송완료_변경_결과 = 주문_배송완료_요청(배송중_송장번호, deliveryEndRequest);
        assertThat(배송중_에서_배송완료_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문 상태가 배송완료로 변경되고
        OrderResponse 배송완료_주문 = 배송중_에서_배송완료_변경_결과.as(OrderResponse.class);
        assertThat(배송완료_주문.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY_END.getStatusName());
    }

    /**
     * Feature: 주문 구매확정 테스트
     *  Background:
     *      given: 구매자 정보로 로그인 되어있음
     *      And: 배송완료된 주문이 존재함
     *
     *  Scenario: 주문 구매확정 시나리오
     *      when: 구매자가 구매확정 처리를 하면
     *      then: 주문 상태가 구매확정으로 변경되고
     *      when: 구매확정된 주문을 환불처리를 시도하면
     *      then: 구매확정된 주문이라 환불처리가 불가능하고
     *      when: 구매확정된 주문을 교환처리를 시도하면
     *      then: 구매확정된 주문이라 교환처리가 불가능하다.
     */
    @Test
    @DisplayName("주문 구매확정 테스트")
    void finishOrderTest(){
        // given

        // when: 구매자가 구매확정 처리를 하면
        ExtractableResponse<Response> 배송완료_에서_구매확정_변경_결과 = 주문_구매확정_요청(배송완료_주문);
        assertThat(배송완료_에서_구매확정_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문 상태가 구매확정으로 변경되고
        OrderResponse 구매확정_주문 = 배송완료_에서_구매확정_변경_결과.as(OrderResponse.class);
        assertThat(구매확정_주문.getOrderStatusName()).isEqualTo(OrderStatus.FINISH.getStatusName());

        // when: 구매확정된 주문을 환불처리를 시도하면
        ExtractableResponse<Response> 구매확정_에서_환불요청_변경_결과 = 주문_환불_요청(구매확정_주문, "환불 신청 합니다.");

        // then: 구매확정된 주문이라 환불처리가 불가능하고
        assertThat(구매확정_에서_환불요청_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // when: 구매확정된 주문을 교환처리를 시도하면
        ExtractableResponse<Response> 구매확정_에서_교환요청_변경_결과 = 주문_교환_요청(구매확정_주문, "교환 신청 합니다.");

        // then: 구매확정된 주문이라 교환처리가 불가능하다.
        assertThat(구매확정_에서_교환요청_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    /**
     * Feature: 주문 교환 테스트
     *  Background:
     *      given: 구매자 정보로 로그인 되어있음
     *      And: 배송이 완료된 주문이 있음
     *
     *  Scenario: 주문 상태 변경 시나리오
     *      when: 배송완료된 주문을 환불처리를 시도하면
     *      then: 주문이 환불처리 된다.
     */
    @Test
    @DisplayName("주문 교환 테스트")
    void orderExchangeTest() {
        // given

        // when: 배송완료된 주문을 환불처리를 시도하면
        ExtractableResponse<Response> 배송완료_에서_교환요청_변경_결과 = 주문_교환_요청(배송완료_주문, "교환 신청 합니다.");
        assertThat(배송완료_에서_교환요청_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문이 환불처리 된다.
        OrderResponse 교환된_주문 = 배송완료_에서_교환요청_변경_결과.as(OrderResponse.class);
        assertThat(교환된_주문.getOrderStatusName()).isEqualTo(OrderStatus.EXCHANGE.getStatusName());
    }

    /**
     * Feature: 주문 환불 테스트
     *  Background:
     *      given: 구매자 정보로 로그인 되어있음
     *      And: 배송이 완료된 주문이 있음
     *
     *  Scenario: 주문 상태 변경 시나리오
     *      when: 배송완료된 주문을 환불처리를 시도하면
     *      then: 주문이 환불처리 된다.
     */
    @Test
    @DisplayName("주문 환불 테스트")
    void orderReturnTest() {
        // given

        // when: 배송완료된 주문을 환불처리를 시도하면
        ExtractableResponse<Response> 배송완료_에서_환불요청_변경_결과 = 주문_환불_요청(배송완료_주문, "환불 신청 합니다.");
        assertThat(배송완료_에서_환불요청_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문이 환불처리 된다.
        OrderResponse 환불된_주문 = 배송완료_에서_환불요청_변경_결과.as(OrderResponse.class);
        assertThat(환불된_주문.getOrderStatusName()).isEqualTo(OrderStatus.REFUND.getStatusName());
    }

    /**
     *  given: 상품이 준비되어 있음
     *  when: 상품의 남은 재고 수 보다 많은 수량을 주문을 시도하면
     *  then: 수량이 너무 많아 주문에 실패한다.
     */
    @Test
    @DisplayName("주문 실패 - 상품 재고 수량보다 많은 수를 주문하면 주문에 실패한다.")
    void order_fail_with_over_quantity() {
        // given

        // when
        ExtractableResponse<Response> 주문_결과 = 주문_생성_요청(상품, 상품.getCount() + 1, 2000, 배송지);

        // then
        assertThat(주문_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(주문_결과.jsonPath().getString("msg")).isEqualTo(
                ErrorCode.OVER_QUANTITY.getMessage()
        );
    }

    public static ExtractableResponse<Response> 주문_주문취소_요청(OrderResponse order, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("cancelDate", LocalDateTime.now());
        params.put("cancelReason", reason);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{id}/cancel", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_출고중_요청(OrderResponse order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().put("/orders/{id}/outing", order.getId())
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 주문_배송중_요청(String invoiceNumber) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/orders/{invoiceNumber}/delivery", invoiceNumber)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_배송완료_요청(String invoiceNumber, DeliveryEndRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("deliveryDate", request.getDeliveryDate());
        params.put("deliveryPlace", request.getDeliveryPlace());
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/orders/{invoiceNumber}/delivery-end", invoiceNumber)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_구매확정_요청(OrderResponse order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{id}/finish", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_환불_요청(OrderResponse order, String reason) {
        Map<String, String> params = new HashMap<>();
        params.put("refundReason", reason);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{id}/refund", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_교환_요청(OrderResponse order, String reason) {
        Map<String, String> params = new HashMap<>();
        params.put("exchangeReason", reason);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{id}/exchange", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_검수중_요청(OrderResponse order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{id}/checking", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_환불완료_요청(OrderResponse order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{id}/refund-end", order.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(ProductResponse product, int quantity, int deliveryFee, DeliveryResponse delivery) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("productId", product.getId());
        itemMap.put("quantity", quantity);

        Map<String, Object> params = new HashMap<>();
        params.put("tid", UUID.randomUUID().toString());
        params.put("payType", PayType.KAKAO_PAY.name());
        params.put("items", List.of(itemMap));
        params.put("deliveryFee", deliveryFee);
        params.put("receiverName", delivery.getReceiverName());
        params.put("zipCode", delivery.getZipCode());
        params.put("address", delivery.getAddress());
        params.put("detailAddress", delivery.getDetailAddress());
        params.put("requestMessage", delivery.getRequestMessage());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/orders")
                .then().log().all()
                .extract();
    }
}
