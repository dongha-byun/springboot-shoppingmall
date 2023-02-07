package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.TestOrderConfig;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Import(TestOrderConfig.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    User user;
    Product product;
    Delivery delivery;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build();
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build();

        user.addDelivery(delivery);
        userRepository.save(user);

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(new Product("상품 1", 22000, 10, category, subCategory));
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void createTest() {
        // given
        OrderRequest orderRequest = new OrderRequest(product.getId(), 3, 3000, delivery.getId());

        // when
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.READY.getStatusName());
        assertThat(orderResponse.getProductName()).isEqualTo("상품 1");
        assertThat(orderResponse.getQuantity()).isEqualTo(3);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(66000);
        assertThat(orderResponse.getDelivery().getReceiverName()).isEqualTo("수령인 1");
    }

    @Test
    @DisplayName("주문 취소 테스트 - 준비중인 주문은 취소가 가능합니다.")
    void cancelTest() {
        // given
        OrderRequest orderRequest = new OrderRequest(product.getId(), 3, 3000, delivery.getId());
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);

        // when
        OrderResponse cancelOrder = orderService.changeOrderStatus(orderResponse.getId(), OrderStatus.CANCEL.name());

        // then
        assertThat(cancelOrder.getOrderStatusName()).isEqualTo(OrderStatus.CANCEL.getStatusName());
    }

    @Test
    @DisplayName("준비 중 상태가 아닌 주문은 취소할 수 없다.")
    void cancelExceptionTest() {
        // given
        Order outingOrder = 특정_주문상태_데이터_생성(OrderStatus.OUTING);
        Order cancelOrder = 특정_주문상태_데이터_생성(OrderStatus.CANCEL);
        Order checkingOrder = 특정_주문상태_데이터_생성(OrderStatus.CHECKING);
        Order endOrder = 특정_주문상태_데이터_생성(OrderStatus.END);
        Order exchangeReqOrder = 특정_주문상태_데이터_생성(OrderStatus.EXCHANGE_REQ);
        Order finishOrder = 특정_주문상태_데이터_생성(OrderStatus.FINISH);
        Order returnEndOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_END);
        Order returnReqOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_REQ);

        // when & then
        assertAll(
                () -> 주문_상태_변경_실패_검증(outingOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(cancelOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(checkingOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(endOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(exchangeReqOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(finishOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(returnEndOrder, OrderStatus.CANCEL),
                () -> 주문_상태_변경_실패_검증(returnReqOrder, OrderStatus.CANCEL)
        );
    }

    @Test
    @DisplayName("주문 접수 테스트 - 준비중인 주문을 접수하면 출고중 상태로 변경된다.")
    void outingTest(){
        // given
        Order order = 특정_주문상태_데이터_생성(OrderStatus.READY);

        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(), OrderStatus.OUTING.name());

        // then
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.OUTING.getStatusName());
    }

    @Test
    @DisplayName("준비 중 상태가 아닌 주문은 접수할 수 없다.")
    void outingExceptionTest() {
        // given
        Order outingOrder = 특정_주문상태_데이터_생성(OrderStatus.OUTING);
        Order cancelOrder = 특정_주문상태_데이터_생성(OrderStatus.CANCEL);
        Order checkingOrder = 특정_주문상태_데이터_생성(OrderStatus.CHECKING);
        Order endOrder = 특정_주문상태_데이터_생성(OrderStatus.END);
        Order exchangeReqOrder = 특정_주문상태_데이터_생성(OrderStatus.EXCHANGE_REQ);
        Order finishOrder = 특정_주문상태_데이터_생성(OrderStatus.FINISH);
        Order returnEndOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_END);
        Order returnReqOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_REQ);

        // when & then
        assertAll(
                () -> 주문_상태_변경_실패_검증(outingOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(cancelOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(checkingOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(endOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(exchangeReqOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(finishOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(returnEndOrder, OrderStatus.OUTING),
                () -> 주문_상태_변경_실패_검증(returnReqOrder, OrderStatus.OUTING)
        );
    }

    @Test
    @DisplayName("배송중 상태 변경 테스트")
    void deliveryTest(){
        // given
        Order order = 특정_주문상태_데이터_생성(OrderStatus.OUTING);

        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(), OrderStatus.DELIVERY.name());

        // then
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());
    }

    @Test
    @DisplayName("배송중 상태 변경 테스트 - 출고중 상태인 주문만 배송 중으로 변경 가능하다.")
    void deliveryExceptionTest(){
        // given
        Order readyOrder = 특정_주문상태_데이터_생성(OrderStatus.READY);
        Order cancelOrder = 특정_주문상태_데이터_생성(OrderStatus.CANCEL);
        Order checkingOrder = 특정_주문상태_데이터_생성(OrderStatus.CHECKING);
        Order endOrder = 특정_주문상태_데이터_생성(OrderStatus.END);
        Order exchangeReqOrder = 특정_주문상태_데이터_생성(OrderStatus.EXCHANGE_REQ);
        Order finishOrder = 특정_주문상태_데이터_생성(OrderStatus.FINISH);
        Order returnEndOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_END);
        Order returnReqOrder = 특정_주문상태_데이터_생성(OrderStatus.RETURN_REQ);

        // when & then
        assertAll(
                () -> 주문_상태_변경_실패_검증(readyOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(cancelOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(checkingOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(endOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(exchangeReqOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(finishOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(returnEndOrder, OrderStatus.DELIVERY),
                () -> 주문_상태_변경_실패_검증(returnReqOrder, OrderStatus.DELIVERY)
        );
    }

    private ThrowableAssertAlternative<IllegalArgumentException> 주문_상태_변경_실패_검증(Order order, OrderStatus status) {
        return assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), status.name()));
    }

    private Order 특정_주문상태_데이터_생성(OrderStatus status) {
        return orderRepository.save(new Order(user, product, 2, delivery, status));
    }

    @Test
    @DisplayName("배송된 주문을 환불요청 한다.")
    void requestReturnOrderTest() {
        // given
        Order endOrder = 특정_주문상태_데이터_생성(OrderStatus.END);
        String returnReason = "환불 요청 합니다.";

        // when
        orderService.requestReturnOrder(endOrder.getId(), returnReason);

        // then
        Order findOrder = orderRepository.findById(endOrder.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.RETURN_REQ);
        assertThat(findOrder.getReturnReason()).isEqualTo(returnReason);
    }

    @Test
    @DisplayName("배송된 주문을 교환요청 한다.")
    void requestExchangeOrderTest() {
        // given
        Order order = 특정_주문상태_데이터_생성(OrderStatus.END);
        String exchangeReason = "교환 요청 합니다.";

        // when
        orderService.requestExchangeOrder(order.getId(), exchangeReason);

        // then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.EXCHANGE_REQ);
        assertThat(findOrder.getExchangeReason()).isEqualTo(exchangeReason);
    }
}