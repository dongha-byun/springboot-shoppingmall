package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.exception.OverQuantityException;
import springboot.shoppingmall.order.application.dto.DeliveryInfoCreateDto;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemCreateDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.application.dto.ResponseUserInformation;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.payment.domain.PayType;

@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    Product product, product2;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;
    MockRestServiceServer mockRestServiceServer;

    int productCount = 10;
    Long partnerId = 10L;

    @BeforeEach
    void beforeEach() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(
                new Product(
                        "상품 1", 22000, productCount, 1.0, 0, LocalDateTime.now(),
                        category, subCategory, partnerId,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "상품 2", 10000, productCount, 1.0, 0, LocalDateTime.now(),
                        category, subCategory, partnerId,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.2",
                        "test-product-code2"
                )
        );
    }

    @Test
    @DisplayName("여러 상품을 한 번에 주문한다.")
    void order_many_products() throws JsonProcessingException {
        // given
        long userId = 10L;
        mockOrderUserInformationServerForGetDiscountRate(userId, 0);

        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItemCreateDto> orderItems = Arrays.asList(
                new OrderItemCreateDto(product.getId(), orderQuantity1, null),
                new OrderItemCreateDto(product2.getId(), orderQuantity2, null)
        );

        DeliveryInfoCreateDto deliveryInfoCreateDto = new DeliveryInfoCreateDto(
                "test-receiver", "010-1234-1234",
                "test-zipcode", "test-address", "test-detail-address",
                "test-request-message");
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                "test-tid", PayType.KAKAO_PAY, orderItems,
                3000, deliveryInfoCreateDto
        );

        // when
        OrderDto order = orderService.createOrder(userId, orderCreateDto);

        // then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getDeliveryInfo().getReceiverPhoneNumber()).isEqualTo("010-1234-1234");
        assertThat(order.getItems()).hasSize(2)
                .extracting("productId", "orderStatus")
                .containsExactly(
                        tuple(product.getId(), OrderStatus.READY),
                        tuple(product2.getId(), OrderStatus.READY)
                );

        // 각각의 상품의 재고가 각 주문 수량 만큼 감소한다.
        assertThat(product.getCount()).isEqualTo(productCount - orderQuantity1);
        assertThat(product2.getCount()).isEqualTo(productCount - orderQuantity2);
    }

    @Test
    @DisplayName("상품 주문 시, 회원등급에 따라 상품 당 가격 할인이 적용된다.")
    void order_discount_by_user_grade() throws JsonProcessingException {
        // 일반등급 회원이 상품을 주문 하면, 주문 상품 가격의 3% 할인가가 적용된다.
        // given
        long userId = 10L;
        int discountRate = 3;
        mockOrderUserInformationServerForGetDiscountRate(userId, discountRate);

        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItemCreateDto> orderItems = Arrays.asList(
                new OrderItemCreateDto(product.getId(), orderQuantity1, null),
                new OrderItemCreateDto(product2.getId(), orderQuantity2, null)
        );

        DeliveryInfoCreateDto deliveryInfoCreateDto = new DeliveryInfoCreateDto(
                "test-receiver", "010-1234-1234",
                "test-zipcode", "test-address", "test-detail-address",
                "test-request-message"
        );
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                "test-tid", PayType.KAKAO_PAY, orderItems,
                3000, deliveryInfoCreateDto
        );

        // when
        OrderDto order = orderService.createOrder(userId, orderCreateDto);

        // then
        List<OrderItemDto> items = order.getItems();

        List<Integer> gradeDiscountAmounts = items.stream()
                .map(OrderItemDto::getGradeDiscountAmount)
                .collect(Collectors.toList());

        List<Integer> discountAmounts = items.stream()
                .map(orderItemResponse -> orderItemResponse.getTotalPrice() * discountRate / 100)
                .collect(Collectors.toList());

        assertThat(gradeDiscountAmounts).isEqualTo(discountAmounts);
    }

    @Test
    @DisplayName("주문 실패 - 재고 수 보다 많은 양을 주문하면 주문에 실패한다.")
    void order_fail_with_quantity_over() throws JsonProcessingException {
        // given
        long userId = 10L;
        mockOrderUserInformationServerForGetDiscountRate(userId, 0);
        int orderQuantity = product.getCount() + 1;
        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto(product.getId(), orderQuantity, null);
        DeliveryInfoCreateDto deliveryInfoCreateDto = new DeliveryInfoCreateDto(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                "test-tid", PayType.KAKAO_PAY,
                List.of(orderItemCreateDto), 0,
                deliveryInfoCreateDto
        );

        // when & then
        assertThatThrownBy(
                () -> orderService.createOrder(userId, orderCreateDto)
        ).isInstanceOf(OverQuantityException.class);
    }

    @Test
    @DisplayName("주문 시, 상품에 쿠폰을 사용하면 금액이 할인된다.")
    void order_used_coupon() throws JsonProcessingException {
        // given
        long userId = 10L;
        mockOrderUserInformationServerForGetDiscountRate(userId, 1);
        UserCoupon userCoupon1 = createCouponAndUserCoupon(userId, "할인쿠폰#1", 5);
        UserCoupon userCoupon2 = createCouponAndUserCoupon(userId, "할인쿠폰#2", 10);

        OrderItemCreateDto orderItem1 = new OrderItemCreateDto(product.getId(), 2, userCoupon1.getId());
        OrderItemCreateDto orderItem2 = new OrderItemCreateDto(product2.getId(), 3, userCoupon2.getId());
        DeliveryInfoCreateDto deliveryInfoCreateDto = new DeliveryInfoCreateDto(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                "test-tid", PayType.KAKAO_PAY,
                Arrays.asList(orderItem1, orderItem2),
                0, deliveryInfoCreateDto
        );

        // when
        OrderDto order = orderService.createOrder(userId, orderCreateDto);

        // then
        assertThat(order.getOrderCode()).isNotNull();
        assertThat(order.getTotalPrice()).isEqualTo(74000);
        assertThat(order.getRealPayPrice()).isEqualTo(68060); // 44000 - 440 - 2200 + 30000 - 300 - 3000

        assertThat(order.getItems()).hasSize(2)
                .extracting("usedCouponId", "couponDiscountAmount")
                .containsExactly(
                        tuple(userCoupon1.getId(), 2200),
                        tuple(userCoupon2.getId(), 3000)
                );
    }

    @Test
    @DisplayName("주문 시, 상품에 쿠폰을 사용하면 쿠폰에 사용일자가 주문일자와 동일하게 처리된다.")
    void set_using_date_used_coupon() throws JsonProcessingException {
        // given
        Long userId = 10L;
        mockOrderUserInformationServerForGetDiscountRate(userId, 0);
        UserCoupon userCoupon1 = createCouponAndUserCoupon(userId, "할인쿠폰#1", 5);
        UserCoupon userCoupon2 = createCouponAndUserCoupon(userId, "할인쿠폰#2", 10);

        OrderItemCreateDto orderItem1 = new OrderItemCreateDto(product.getId(), 2, userCoupon1.getId());
        OrderItemCreateDto orderItem2 = new OrderItemCreateDto(product2.getId(), 3, userCoupon2.getId());
        DeliveryInfoCreateDto deliveryInfoCreateDto = new DeliveryInfoCreateDto(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                "test-tid", PayType.KAKAO_PAY,
                Arrays.asList(orderItem1, orderItem2),
                0, deliveryInfoCreateDto
        );

        // when
        OrderDto order = orderService.createOrder(userId, orderCreateDto);

        // then
        assertThat(order.getOrderCode()).isNotNull();
        assertThat(order.getOrderDate()).isNotNull();

        UserCoupon usedUserCoupon = userCouponRepository.findById(userCoupon1.getId()).orElseThrow();
        assertThat(usedUserCoupon.getUsingDate()).isNotNull();
    }

    private void mockOrderUserInformationServerForGetDiscountRate(long userId, int discountRate) throws JsonProcessingException {
        ResponseUserInformation response = new ResponseUserInformation(discountRate);
        String responseContent = objectMapper.writeValueAsString(response);

        mockRestServiceServer.expect(
                        requestTo("/user/" + userId + "/grade-info")
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(responseContent)
                );
    }

    private UserCoupon createCouponAndUserCoupon(Long userId, String name, int discountRate) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(10);
        LocalDateTime toDate = LocalDateTime.now().plusDays(10);
        Coupon coupon = Coupon.create(name, fromDate, toDate, discountRate, partnerId);
        coupon.addUserCoupon(userId);
        couponRepository.save(coupon);

        return coupon.getUserCoupons().get(0);
    }
}