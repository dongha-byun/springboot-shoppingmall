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
import springboot.shoppingmall.authorization.LoginAcceptanceTest;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.exception.ErrorCode;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.DeliveryEndRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.delivery.domain.Delivery;
import springboot.shoppingmall.delivery.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.PayType;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserGrade;
import springboot.shoppingmall.userservice.user.domain.UserGradeInfo;
import springboot.shoppingmall.userservice.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.DeliveryResponse;
public class OrderAcceptanceTest extends AcceptanceProductTest {

    @Autowired
    CouponRepository couponRepository;
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
                new OrderItem(product1, 2, OrderStatus.DELIVERY_END),
                new OrderItem(product2, 3, OrderStatus.DELIVERY_END)
        );
        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage()
        );
        Order order = orderRepository.save(
                new Order("test-order-code", user.getId(), orderItems, orderDeliveryInfo)
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
        assertThat(주문_생성_결과.jsonPath().getString("deliveryInfo.receiverName")).isEqualTo(배송지.getReceiverName());
        assertThat(주문_생성_결과.jsonPath().getString("deliveryInfo.receiverPhoneNumber")).isEqualTo(배송지.getReceiverPhoneNumber());
        assertThat(주문_생성_결과.jsonPath().getList("items.orderStatusName")).containsExactly(
                OrderStatus.READY.getStatusName()
        );
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
        Map<String, Object> item1 = new HashMap<>();
        item1.put("productId", 상품.getId());
        item1.put("quantity", 3);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("productId", 상품2.getId());
        item2.put("quantity", 5);

        List<Map<String, Object>> 주문_상품_목록 = Arrays.asList(item1, item2);

        // when
        ExtractableResponse<Response> 결과 = 주문_상품_다건_생성_요청(주문_상품_목록, 3000, 배송지);

        // then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getList("items")).hasSize(2);
    }

    /**
     * Feature: 최초 주문 (여러 상품)
     *  Background:
     *      given: 로그인한 사용자
     *      And: 사용자가 VIP 등급임
     *      And: 등록되어있는 상품들
     *      And: 사용자가 등록해놓은 배송지 정보가 존재함
     *
     *  Scenario: 최초 주문 생성
     *      when: 구매하고자 하는 상품정보와  갯수, 배송지 정보를 가지고 주문을 생성하면
     *      then: 주문 내역 조회 시, 주문된 내용이 조회된다.
     */
    @Test
    @DisplayName("회원등급 할인을 받아서 여러 상품을 구매한다.")
    void order_with_many_products_and_discount_grade() {
        // given
        String loginId = "vip_user1";
        String password = "vip_user1!";
        userRepository.save(
                new User(
                        "VIP 회원", loginId, password, "010-2344-2344",
                        LocalDateTime.of(2022, 12, 22, 13, 15, 10),
                        0, false, new UserGradeInfo(UserGrade.VIP, 50, 150000)
                )
        );
        TokenResponse VIP_로그인정보 = LoginAcceptanceTest.로그인(loginId, password).as(TokenResponse.class);

        Map<String, Object> item1 = new HashMap<>();
        item1.put("productId", 상품.getId());
        item1.put("quantity", 3);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("productId", 상품2.getId());
        item2.put("quantity", 5);

        List<Map<String, Object>> 주문_상품_목록 = Arrays.asList(item1, item2);

        // when
        ExtractableResponse<Response> 결과 = 주문_상품_다건_생성_요청(주문_상품_목록, 3000, 배송지, VIP_로그인정보);

        // then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getList("items")).hasSize(2);
        assertThat(결과.jsonPath().getList("items.gradeDiscountAmount", Integer.class)).containsExactly(
                (상품.getPrice() * 3) * UserGrade.VIP.getDiscountRate() / 100,
                (상품2.getPrice() * 5) * UserGrade.VIP.getDiscountRate() / 100
        );
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
        OrderItemResponse 주문_상품 = 주문.getItems().get(0);

        // when
        ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_출고중_요청(주문, 주문_상품);
        OrderItemResponse 출고중_상품 = 주문_상태_변경_결과.as(OrderItemResponse.class);

        // then
        assertThat(주문_상태_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(주문_상태_변경_결과.jsonPath().getString("invoiceNumber")).isEqualTo(
                출고중_상품.getInvoiceNumber()
        );
        assertThat(주문_상태_변경_결과.jsonPath().getString("orderStatusName")).isEqualTo(
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
        OrderResponse 주문 = 주문_생성_요청(상품, 3, 3000, 배송지).as(OrderResponse.class);
        OrderItemResponse 준비중_주문_상품 = 첫_번째_주문_상품(주문);
        assertThat(준비중_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.READY.getStatusName());

        // when : 준비 중인 주문을 출고중으로 변경하면
        ExtractableResponse<Response> 준비중_에서_출고중_변경_결과 = 주문_출고중_요청(주문, 준비중_주문_상품);
        assertThat(준비중_에서_출고중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 출고중으로 변경되고
        OrderItemResponse 출고중_주문_상품 = 준비중_에서_출고중_변경_결과.as(OrderItemResponse.class);
        assertThat(출고중_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.OUTING.getStatusName());

        // when : 출고중인 주문 상태를 주문 취소로 변경하면
        String cancelReason = "주문 취소 합니다.";
        ExtractableResponse<Response> 출고중_에서_주문취소_변경_결과 = 주문_주문취소_요청(주문, 출고중_주문_상품, cancelReason);

        // then : 준비 중인 주문이 아니므로, 주문 취소 상태로 변경되지 않고
        assertThat(출고중_에서_주문취소_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // when : 출고중인 주문 상태를 배송 중으로 변경하면
        LocalDateTime deliveryStartDate = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        String 출고중_송장번호 = 출고중_주문_상품.getInvoiceNumber();
        ExtractableResponse<Response> 출고중_에서_배송중_변경_결과 = 주문_배송중_요청(출고중_송장번호, deliveryStartDate);
        assertThat(출고중_에서_배송중_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then : 주문 상태가 배송중으로 변경되고
        OrderItemResponse 배송중_주문_상품 = 출고중_에서_배송중_변경_결과.as(OrderItemResponse.class);
        assertThat(배송중_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());

        // when: 배송이 완료되어 배송완료 처리를 하면
        LocalDateTime deliveryDate = LocalDateTime.of(2023, 5, 5, 15, 30, 30);
        String deliveryPlace = "무인택배함";
        DeliveryEndRequest deliveryEndRequest = new DeliveryEndRequest(deliveryDate, deliveryPlace);

        String 배송중_송장번호 = 배송중_주문_상품.getInvoiceNumber();
        ExtractableResponse<Response> 배송중_에서_배송완료_변경_결과 = 주문_배송완료_요청(배송중_송장번호, deliveryEndRequest);
        assertThat(배송중_에서_배송완료_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문 상태가 배송완료로 변경되고
        OrderItemResponse 배송완료_주문_상품 = 배송중_에서_배송완료_변경_결과.as(OrderItemResponse.class);
        assertThat(배송완료_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY_END.getStatusName());
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
        OrderItemResponse 배송완료_주문_상품 = 첫_번째_주문_상품(배송완료_주문);
        ExtractableResponse<Response> 배송완료_에서_구매확정_변경_결과 = 주문_구매확정_요청(배송완료_주문, 배송완료_주문_상품);
        assertThat(배송완료_에서_구매확정_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문 상태가 구매확정으로 변경되고
        OrderItemResponse 구매확정_주문_상품 = 배송완료_에서_구매확정_변경_결과.as(OrderItemResponse.class);
        assertThat(구매확정_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.FINISH.getStatusName());

        // when: 구매확정된 주문을 환불처리를 시도하면
        ExtractableResponse<Response> 구매확정_에서_환불요청_변경_결과 = 주문_환불_요청(배송완료_주문, 구매확정_주문_상품, "환불 신청 합니다.");

        // then: 구매확정된 주문이라 환불처리가 불가능하고
        assertThat(구매확정_에서_환불요청_변경_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // when: 구매확정된 주문을 교환처리를 시도하면
        ExtractableResponse<Response> 구매확정_에서_교환요청_변경_결과 = 주문_교환_요청(배송완료_주문, 구매확정_주문_상품, "교환 신청 합니다.");

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
        OrderItemResponse 주문_상품 = 첫_번째_주문_상품(배송완료_주문);
        ExtractableResponse<Response> 배송완료_에서_교환요청_변경_결과 = 주문_교환_요청(배송완료_주문, 주문_상품, "교환 신청 합니다.");
        assertThat(배송완료_에서_교환요청_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문이 환불처리 된다.
        OrderItemResponse 교환된_주문_상품 = 배송완료_에서_교환요청_변경_결과.as(OrderItemResponse.class);
        assertThat(교환된_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.EXCHANGE.getStatusName());
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
        OrderItemResponse 주문_상품 = 첫_번째_주문_상품(배송완료_주문);
        ExtractableResponse<Response> 배송완료_에서_환불요청_변경_결과 = 주문_환불_요청(배송완료_주문, 주문_상품, "환불 신청 합니다.");
        assertThat(배송완료_에서_환불요청_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then: 주문이 환불처리 된다.
        OrderItemResponse 환불된_주문_상품 = 배송완료_에서_환불요청_변경_결과.as(OrderItemResponse.class);
        assertThat(환불된_주문_상품.getOrderStatusName()).isEqualTo(OrderStatus.REFUND.getStatusName());
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

    /**
     *  given: 상품이 준비되어 있음
     *  And: 쿠폰이 준비되어 있음
     *  when: 상품 주문 시, 쿠폰을 적용하면
     *  then: 쿠폰이 사용처리 되고, 총 결제금액이 할인 된다.
     */
    @Test
    @DisplayName("주문 시 쿠폰을 사용하면, 결제금액이 할인된다.")
    void order_used_coupon() {
        // given
        Coupon coupon1 = createCoupon("할인쿠폰#1", 5);
        Coupon coupon2 = createCoupon("할인쿠폰#2", 8);
        coupon1.addUserCoupon(인수테스터1.getId());
        coupon2.addUserCoupon(인수테스터1.getId());
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);

        Map<String, String> deliveryInfoMap = new HashMap<>();
        deliveryInfoMap.put("receiverName", "수령인1");
        deliveryInfoMap.put("receiverPhoneNumber", "010-2222-3333");
        deliveryInfoMap.put("zipCode", "09822");
        deliveryInfoMap.put("address", "서울시 영등포구");
        deliveryInfoMap.put("detailAddress", "상세주소 1");
        deliveryInfoMap.put("requestMessage", "택배 보관함에 넣어주세요.");

        Long userCoupon1Id = coupon1.getUserCoupons().get(0).getId();
        Long userCoupon2Id = coupon2.getUserCoupons().get(0).getId();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("productId", 상품.getId()); // 10000원
        item1.put("quantity", 2);
        item1.put("usedCouponId", userCoupon1Id); // 5% = 500원 * 2 = 1000원

        Map<String, Object> item2 = new HashMap<>();
        item2.put("productId", 상품2.getId()); // 11000원
        item2.put("quantity", 5);
        item2.put("usedCouponId", userCoupon2Id); // 8% = 880원 * 5 = 4400원

        Map<String, Object> params = new HashMap<>();
        params.put("tid", "test-tid");
        params.put("payType", PayType.CARD.name());
        params.put("items", Arrays.asList(item1, item2));
        params.put("deliveryFee", 0);
        params.put("deliveryInfo", deliveryInfoMap);

        // when
        ExtractableResponse<Response> 쿠폰_주문_결과 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/orders")
                .then().log().all()
                .extract();

        // then
        assertThat(쿠폰_주문_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(쿠폰_주문_결과.jsonPath().getInt("totalPrice")).isEqualTo(75000);
        assertThat(쿠폰_주문_결과.jsonPath().getInt("realPayPrice")).isEqualTo(68850);
        assertThat(쿠폰_주문_결과.jsonPath().getList("items.usedCouponId", Long.class)).containsExactly(
                userCoupon1Id, userCoupon2Id
        );
        assertThat(쿠폰_주문_결과.jsonPath().getList("items.couponDiscountAmount", Integer.class)).containsExactly(
                1000, 4400
        );
        assertThat(쿠폰_주문_결과.jsonPath().getList("items.gradeDiscountAmount", Integer.class)).containsExactly(
                200, 550
        );

        ExtractableResponse<Response> 사용_가능한_쿠폰_목록 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().get("/order/coupons?partnersId={partnersId}", partnerId)
                .then().log().all()
                .extract();
        assertThat(사용_가능한_쿠폰_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(사용_가능한_쿠폰_목록.jsonPath().getList("id")).hasSize(0);
    }

    private Coupon createCoupon(String name, int discountRate) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(10);
        LocalDateTime toDate = LocalDateTime.now().plusDays(10);
        return Coupon.create(name, fromDate, toDate, discountRate, partnerId);
    }

    public static ExtractableResponse<Response> 주문_주문취소_요청(OrderResponse order,
                                                           OrderItemResponse orderItem,
                                                           String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("cancelReason", reason);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{orderId}/{orderItemId}/cancel", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_출고중_요청(OrderResponse order, OrderItemResponse orderItem) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(판매자_로그인토큰))
                .when().put("/orders/{orderId}/{orderItemId}/outing", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 주문_배송중_요청(String invoiceNumber, LocalDateTime deliveryStartDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryStartDate", deliveryStartDate);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(map)
                .when().put("/orders/{invoiceNumber}/delivery", invoiceNumber)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_배송완료_요청(String invoiceNumber, DeliveryEndRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("deliveryCompleteDate", request.getDeliveryCompleteDate());
        params.put("deliveryPlace", request.getDeliveryPlace());
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/orders/{invoiceNumber}/delivery-end", invoiceNumber)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_구매확정_요청(OrderResponse order, OrderItemResponse orderItem) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{orderId}/{orderItemId}/finish", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_환불_요청(OrderResponse order, OrderItemResponse orderItem, String reason) {
        Map<String, String> params = new HashMap<>();
        params.put("refundReason", reason);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{orderId}/{orderItemId}/refund", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_교환_요청(OrderResponse order, OrderItemResponse orderItem, String reason) {
        Map<String, String> params = new HashMap<>();
        params.put("exchangeReason", reason);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().put("/orders/{orderId}/{orderItemId}/exchange", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_검수중_요청(OrderResponse order, OrderItemResponse orderItem) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{orderId}/{orderItemId}/checking", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_환불완료_요청(OrderResponse order, OrderItemResponse orderItem) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .when().put("/orders/{orderId}/{orderItemId}/refund-end", order.getId(), orderItem.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(ProductResponse product, int quantity, int deliveryFee, DeliveryResponse delivery) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("productId", product.getId());
        itemMap.put("quantity", quantity);

        Map<String, String> deliveryInfoMap = new HashMap<>();
        deliveryInfoMap.put("receiverName", delivery.getReceiverName());
        deliveryInfoMap.put("receiverPhoneNumber", delivery.getReceiverPhoneNumber());
        deliveryInfoMap.put("zipCode", delivery.getZipCode());
        deliveryInfoMap.put("address", delivery.getAddress());
        deliveryInfoMap.put("detailAddress", delivery.getDetailAddress());
        deliveryInfoMap.put("requestMessage", delivery.getRequestMessage());

        Map<String, Object> params = new HashMap<>();
        params.put("tid", UUID.randomUUID().toString());
        params.put("payType", PayType.KAKAO_PAY.name());
        params.put("items", List.of(itemMap));
        params.put("deliveryFee", deliveryFee);
        params.put("deliveryInfo", deliveryInfoMap);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(로그인정보))
                .body(params)
                .when().post("/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상품_다건_생성_요청(
            List<Map<String, Object>> items, int deliveryFee, DeliveryResponse delivery) {
        return 주문_상품_다건_생성_요청(items, deliveryFee, delivery, 로그인정보);
    }

    public static ExtractableResponse<Response> 주문_상품_다건_생성_요청(
            List<Map<String, Object>> items, int deliveryFee, DeliveryResponse delivery, TokenResponse tokenResponse) {

        Map<String, String> deliveryInfoMap = new HashMap<>();
        deliveryInfoMap.put("receiverName", delivery.getReceiverName());
        deliveryInfoMap.put("receiverPhoneNumber", delivery.getReceiverPhoneNumber());
        deliveryInfoMap.put("zipCode", delivery.getZipCode());
        deliveryInfoMap.put("address", delivery.getAddress());
        deliveryInfoMap.put("detailAddress", delivery.getDetailAddress());
        deliveryInfoMap.put("requestMessage", delivery.getRequestMessage());

        Map<String, Object> params = new HashMap<>();
        params.put("tid", UUID.randomUUID().toString());
        params.put("payType", PayType.KAKAO_PAY.name());
        params.put("items", items);
        params.put("deliveryFee", deliveryFee);
        params.put("deliveryInfo", deliveryInfoMap);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(createAuthorizationHeader(tokenResponse))
                .body(params)
                .when().post("/orders")
                .then().log().all()
                .extract();
    }
}
