package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;
import springboot.shoppingmall.coupon.domain.UserCoupon;
import springboot.shoppingmall.coupon.domain.UserCouponRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.DeliveryInfoRequest;
import springboot.shoppingmall.order.dto.OrderItemRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.exception.OverQuantityException;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.delivery.domain.Delivery;
import springboot.shoppingmall.user.domain.PayType;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserGrade;
import springboot.shoppingmall.userservice.user.domain.UserGradeInfo;
import springboot.shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    User user;
    Product product;
    Product product2;
    Delivery delivery;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserCouponRepository userCouponRepository;

    int productCount = 10;
    Long partnerId = 10L;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .userName("테스터1").email("test1@test.com").password("test1!").telNo("010-0000-0000")
                .build();
        userRepository.save(user);

        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").receiverPhoneNumber("010-1234-1234")
                .zipCode("10010").address("서울시 동작구 사당동").detailAddress("101호")
                .requestMessage("도착 시 연락주세요.")
                .userId(user.getId())
                .build();

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
    void order_many_products_test() {
        // given
        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItemRequest> itemRequests = Arrays.asList(
                new OrderItemRequest(product.getId(), orderQuantity1),
                new OrderItemRequest(product2.getId(), orderQuantity2)
        );

        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "test-receiver", "010-1234-1234",
                "test-zipcode", "test-address", "test-detail-address",
                "test-request-message");
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(), itemRequests,
                3000, deliveryInfoRequest
        );

        // when
        OrderResponse order = orderService.createOrder(user.getId(), orderRequest);

        // then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getDeliveryInfo().getReceiverPhoneNumber()).isEqualTo("010-1234-1234");

        assertThat(order.getItems()).hasSize(2);

        List<OrderItemResponse> orderItems = order.getItems();
        List<String> statusNames = orderItems.stream()
                .map(OrderItemResponse::getOrderStatusName)
                .collect(Collectors.toList());
        assertThat(statusNames).containsExactly(
                OrderStatus.READY.getStatusName(), OrderStatus.READY.getStatusName()
        );

        // 각각의 상품의 재고가 각 주문 수량 만큼 감소한다.
        assertThat(product.getCount()).isEqualTo(productCount - orderQuantity1);
        assertThat(product2.getCount()).isEqualTo(productCount - orderQuantity2);
    }

    @Test
    @DisplayName("상품 주문 시, 회원등급에 따라 상품 당 가격 할인이 적용된다.")
    void order_discount_by_user_grade() {
        // 일반등급 회원이 상품을 주문 하면, 주문 상품 가격의 3% 할인가가 적용된다.
        // given
        User tester2 = new User(
                "테스터2", "tester2", "tester2@", "010-2222-3333",
                LocalDateTime.of(2022, 12, 15, 15, 0),
                0, false, new UserGradeInfo(UserGrade.REGULAR, 10, 50000)
        );
        userRepository.save(tester2);

        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItemRequest> itemRequests = Arrays.asList(
                new OrderItemRequest(product.getId(), orderQuantity1),
                new OrderItemRequest(product2.getId(), orderQuantity2)
        );

        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "test-receiver", "010-1234-1234",
                "test-zipcode", "test-address", "test-detail-address",
                "test-request-message"
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(), itemRequests,
                3000, deliveryInfoRequest
        );

        // when
        OrderResponse order = orderService.createOrder(tester2.getId(), orderRequest);

        // then
        List<OrderItemResponse> items = order.getItems();

        List<Integer> gradeDiscountAmounts = items.stream()
                .map(OrderItemResponse::getGradeDiscountAmount)
                .collect(Collectors.toList());

        List<Integer> discountAmounts = items.stream()
                .map(orderItemResponse -> orderItemResponse.getTotalPrice() * UserGrade.REGULAR.getDiscountRate() / 100)
                .collect(Collectors.toList());

        assertThat(gradeDiscountAmounts).isEqualTo(discountAmounts);
    }

    @Test
    @DisplayName("주문 실패 - 재고 수 보다 많은 양을 주문하면 주문에 실패한다.")
    void order_fail_with_quantity_over() {
        // given
        int orderQuantity = product.getCount() + 1;
        OrderItemRequest orderItemRequest = new OrderItemRequest(product.getId(), orderQuantity);
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(),
                List.of(orderItemRequest), 0,
                deliveryInfoRequest
        );

        // when & then
        assertThatThrownBy(
                () -> orderService.createOrder(user.getId(), orderRequest)
        ).isInstanceOf(OverQuantityException.class);
    }

    @Test
    @DisplayName("주문 시, 상품에 쿠폰을 사용하면 금액이 할인된다.")
    void order_used_coupon() {
        // given
        UserCoupon userCoupon1 = createCouponAndUserCoupon("할인쿠폰#1", 5);
        UserCoupon userCoupon2 = createCouponAndUserCoupon("할인쿠폰#2", 10);

        OrderItemRequest orderItem1 = new OrderItemRequest(product.getId(), 2, userCoupon1.getId());
        OrderItemRequest orderItem2 = new OrderItemRequest(product2.getId(), 3, userCoupon2.getId());
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(),
                Arrays.asList(orderItem1, orderItem2),
                0, deliveryInfoRequest
        );

        // when
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);
        assertThat(user.getUserGradeInfo().getGrade()).isEqualTo(UserGrade.NORMAL);

        // then
        assertThat(orderResponse.getOrderCode()).isNotNull();
        assertThat(orderResponse.getTotalPrice()).isEqualTo(74000);
        assertThat(orderResponse.getRealPayPrice()).isEqualTo(68060); // 44000 - 440 - 2200 + 30000 - 300 - 3000

        assertThat(orderResponse.getItems()).hasSize(2)
                .extracting("usedCouponId", "couponDiscountAmount")
                .containsExactly(
                        tuple(userCoupon1.getId(), 2200),
                        tuple(userCoupon2.getId(), 3000)
                );
    }

    @Test
    @DisplayName("주문 시, 상품에 쿠폰을 사용하면 쿠폰에 사용일자가 주문일자와 동일하게 처리된다.")
    void set_using_date_used_coupon() {
        // given
        UserCoupon userCoupon1 = createCouponAndUserCoupon("할인쿠폰#1", 5);
        UserCoupon userCoupon2 = createCouponAndUserCoupon("할인쿠폰#2", 10);

        OrderItemRequest orderItem1 = new OrderItemRequest(product.getId(), 2, userCoupon1.getId());
        OrderItemRequest orderItem2 = new OrderItemRequest(product2.getId(), 3, userCoupon2.getId());
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(),
                Arrays.asList(orderItem1, orderItem2),
                0, deliveryInfoRequest
        );

        // when
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);
        assertThat(user.getUserGradeInfo().getGrade()).isEqualTo(UserGrade.NORMAL);

        // then
        assertThat(orderResponse.getOrderCode()).isNotNull();
        assertThat(orderResponse.getOrderDate()).isNotNull();

        UserCoupon usedUserCoupon = userCouponRepository.findById(userCoupon1.getId()).orElseThrow();
        assertThat(usedUserCoupon.getUsingDate()).isNotNull();
    }

    private UserCoupon createCouponAndUserCoupon(String name, int discountRate) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(10);
        LocalDateTime toDate = LocalDateTime.now().plusDays(10);
        Coupon coupon = Coupon.create(name, fromDate, toDate, discountRate, partnerId);
        coupon.addUserCoupon(user.getId());
        couponRepository.save(coupon);

        return coupon.getUserCoupons().get(0);
    }
}