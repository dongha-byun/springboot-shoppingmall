package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static springboot.shoppingmall.utils.DateUtils.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.TestOrderConfig;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.DeliveryInfoRequest;
import springboot.shoppingmall.order.dto.OrderItemRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.exception.OverQuantityException;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.PayType;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.domain.UserGradeInfo;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.utils.DateUtils;

@Import(TestOrderConfig.class)
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
    OrderRepository orderRepository;

    int productCount = 10;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build();
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").receiverPhoneNumber("010-1234-1234")
                .zipCode("10010").address("서울시 동작구 사당동").detailAddress("101호")
                .requestMessage("도착 시 연락주세요.").build();

        user.addDelivery(delivery);
        userRepository.save(user);

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(
                new Product(
                        "상품 1", 22000, productCount, 1.0, 0, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "상품 2", 10000, productCount, 1.0, 0, LocalDateTime.now(),
                        category, subCategory, 10L,
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
    @DisplayName("주문 취소 - 준비 중인 주문 상품에 대해서는 별도로 주문취소 처리가 가능하다.")
    void order_item_cancel_test() {
        // given
        int orderQuantity = 3;
        OrderItemRequest orderItemRequest = new OrderItemRequest(product.getId(), orderQuantity);
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage()
        );
        OrderRequest orderRequest = new OrderRequest(
                "test-tid", PayType.KAKAO_PAY.name(),
                List.of(orderItemRequest), 3000, deliveryInfoRequest
        );
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);
        Long orderId = orderResponse.getId();
        Long orderItemId = orderResponse.getItems().get(0).getId();

        // when
        String cancelReason = "단순변심으로 구매 취소합니다.";
        LocalDateTime cancelDate = LocalDateTime.of(2023, 5, 9, 13, 10, 12);
        OrderItemResponse cancelItem = orderService.cancel(orderId, orderItemId, cancelDate, cancelReason);

        // then
        assertThat(cancelItem.getOrderStatusName()).isEqualTo(OrderStatus.CANCEL.getStatusName());
        assertThat(cancelItem.getCancelDate()).isEqualTo(toStringOfLocalDateTIme(cancelDate));
        assertThat(cancelItem.getCancelReason()).isEqualTo(cancelReason);

        // 주문을 취소하면 상품 갯수를 원래대로 되돌린다.
        Product findProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(findProduct.getCount()).isEqualTo(productCount);
    }

    @Test
    @DisplayName("교환 신청 - 배송이 완료된 주문 상품에 대해 교환신청을 할 수 있다.")
    void exchange_test() {
        // 배송이 완료된 주문상품이 존재한다.
        // 해당 주문상품을 교환신청 한다.
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when

        // then
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
    @DisplayName("구매 확정 - 배송이 완료된 상품을 구매확정 처리 한다. 동시에 사용자의 주문횟수/주문금액을 증가시킨다.")
    void order_finish_and_user_grade_info_update_test() {
        // given
        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.DELIVERY_END),
                new OrderItem(product2, orderQuantity2, OrderStatus.DELIVERY_END)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "finish-order-code", user.getId(), items, orderDate,
                "덩라", "010-1234-1234",
                "01234", "서울시 테스트구 테스트동", "덩라빌딩 301호",
                "조심히 오세요."
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        // when
        orderService.finish(savedOrder.getId(), orderItem1.getId());

        // then
        assertThat(orderItem1.getOrderStatus()).isEqualTo(OrderStatus.FINISH);

        // 판매수량 증가
        Product orderItem1Product = orderItem1.getProduct();
        assertThat(orderItem1Product.getId()).isEqualTo(product.getId());
        assertThat(product.getSalesVolume()).isEqualTo(orderQuantity1);

        // 구매자의 주문횟수/금액 처리
        User orderUser = userRepository.findById(user.getId()).orElseThrow();
        UserGradeInfo userGradeInfo = orderUser.getUserGradeInfo();
        assertThat(userGradeInfo.getOrderCount()).isEqualTo(1);
        assertThat(userGradeInfo.getAmount()).isEqualTo(orderItem1.getQuantity() * product.getPrice());
    }
}