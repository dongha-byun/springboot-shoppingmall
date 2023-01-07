package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
        OrderResponse cancelOrder = orderService.cancelOrder(orderResponse.getId());

        // then
        assertThat(cancelOrder.getOrderStatusName()).isEqualTo(OrderStatus.CANCEL.getStatusName());
    }

    @Test
    @DisplayName("준비 중 상태가 아닌 주문은 취소할 수 없다.")
    void cancelExceptionTest() {
        // given
        Order outingOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.OUTING));
        Order cancelOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.CANCEL));
        Order checkingOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.CHECKING));
        Order endOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.END));
        Order exchangeReqOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.EXCHANGE_REQ));
        Order finishOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.FINISH));
        Order returnEndOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.RETURN_END));
        Order returnReqOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.RETURN_REQ));

        // when & then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(outingOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(cancelOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(checkingOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.cancelOrder(endOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(exchangeReqOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(finishOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(returnEndOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> orderService.cancelOrder(returnReqOrder.getId()))
        );
    }

    @Test
    @DisplayName("주문 접수 테스트 - 준비중인 주문을 접수하면 출고중 상태로 변경된다.")
    void outingTest(){
        // given
        Order order = orderRepository.save(new Order(user, product, 2, delivery, OrderStatus.READY));

        // when
        OrderResponse orderResponse = orderService.outingOrder(order.getId());

        // then
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.OUTING.getStatusName());
    }

    @Test
    @DisplayName("준비 중 상태가 아닌 주문은 접수할 수 없다.")
    void outingExceptionTest() {
        // given
        Order outingOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.OUTING));
        Order cancelOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.CANCEL));
        Order checkingOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.CHECKING));
        Order endOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.END));
        Order exchangeReqOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.EXCHANGE_REQ));
        Order finishOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.FINISH));
        Order returnEndOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.RETURN_END));
        Order returnReqOrder = orderRepository.save(new Order(user, product, 3, delivery, OrderStatus.RETURN_REQ));

        // when & then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(outingOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(cancelOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(checkingOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(endOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(exchangeReqOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(finishOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(returnEndOrder.getId())),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> orderService.outingOrder(returnReqOrder.getId()))
        );
    }

    @Test
    @DisplayName("배송중 상태 변경 테스트")
    void deliveryTest(){
        // given
        Order order = orderRepository.save(new Order(user, product, 2, delivery, OrderStatus.OUTING));

        // when
        OrderResponse orderResponse = orderService.deliveryOrder(order.getId());

        // then
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());
    }

    @Test
    @DisplayName("배송중 상태 변경 테스트 - 출고중 상태인 주문만 배송 중으로 변경 가능하다.")
    void deliveryExceptionTest(){
        // given
        Order order = orderRepository.save(new Order(user, product, 2, delivery, OrderStatus.OUTING));

        // when
        OrderResponse orderResponse = orderService.deliveryOrder(order.getId());

        // then
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());
    }
}