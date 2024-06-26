package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.application.dto.ExchangeEndResultDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.handler.OrderCanceledEventHandler;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class OrderStatusChangeServiceTest extends IntegrationTest {

    @Autowired
    OrderStatusChangeService orderStatusChangeService;

    @MockBean
    OrderUserInterfaceService orderUserInterfaceService;

    @MockBean
    OrderDeliveryInterfaceServiceImpl orderDeliveryInterfaceService;

    @MockBean
    OrderCanceledEventHandler orderCanceledEventHandler;

    Product product, product2;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrderRepository orderRepository;

    int productCount = 10;

    OrderDeliveryInfo orderDeliveryInfo;

    Long userId = 10L;

    @BeforeEach
    void beforeEach() {
        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인 1", "010-1234-1234",
                "10010", "서울시 동작구 사당동", "101호",
                "도착 시 연락주세요."
        );

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        LocalDateTime registerDate = LocalDateTime.of(2023, 5, 15, 0, 0, 0);
        product = saveProduct("상품 1", 22000, productCount, 1.0, 0,
                category.getId(), subCategory.getId(), 10L, registerDate);
        product2 = saveProduct("상품 2", 10000, productCount, 1.0, 0,
                category.getId(), subCategory.getId(), 10L, registerDate);
    }

    @Test
    @DisplayName("출고 중 - 주문이 들어온 상품을 출고한다.")
    void outing() {
        // given
        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.PREPARED),
                new OrderItem(product2, orderQuantity2, OrderStatus.PREPARED)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "finish-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        mockingCreateInvoiceNumber();

        // when
        OrderItemDto outingItemDto = orderStatusChangeService.outing(orderItem1.getId());

        // then
        assertThat(outingItemDto.getId()).isEqualTo(orderItem1.getId());
        assertThat(outingItemDto.getOrderStatus()).isEqualTo(OrderStatus.OUTING);
        assertThat(outingItemDto.getInvoiceNumber()).isNotNull();
    }

    @Test
    @DisplayName("구매 확정 - 배송이 완료된 상품을 구매확정 처리 한다. 동시에 사용자의 주문횟수/주문금액을 증가시킨다.")
    void finish_and_user_grade_info_update() {
        // given
        mockingIncreaseOrderAmounts(userId, 100000);

        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.DELIVERY_END),
                new OrderItem(product2, orderQuantity2, OrderStatus.DELIVERY_END)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "finish-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        // when
        orderStatusChangeService.finish(orderItem1.getId());

        // then
        assertThat(orderItem1.getOrderStatus()).isEqualTo(OrderStatus.FINISH);

        // 판매수량 증가
        Product orderItem1Product = orderItem1.getProduct();
        assertThat(orderItem1Product.getId()).isEqualTo(product.getId());
        assertThat(product.getSalesVolume()).isEqualTo(orderQuantity1);
    }

    @Test
    @DisplayName("상품 검수중 - 반품된 상품의 상태를 점검한다.")
    void checking() {
        // given
        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.REFUND),
                new OrderItem(product2, orderQuantity2, OrderStatus.REFUND)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "refund-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        // when
        OrderItemDto checkingItemDto = orderStatusChangeService.checking(orderItem1.getId());

        // then
        assertThat(checkingItemDto.getId()).isEqualTo(orderItem1.getId());
        assertThat(checkingItemDto.getOrderStatus()).isEqualTo(OrderStatus.CHECKING);
    }

    @Test
    @DisplayName("환불 완료 - 환불이 요청된 상품의 검수가 종료되면, 환불 완료 처리를 한다.")
    void refund_end() {
        // given
        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.CHECKING),
                new OrderItem(product2, orderQuantity2, OrderStatus.CHECKING)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "refund-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        doNothing().when(orderCanceledEventHandler).handle(any());

        // when
        OrderItemDto checkingItemDto = orderStatusChangeService.refundEnd(orderItem1.getId());

        // then
        assertThat(checkingItemDto.getId()).isEqualTo(orderItem1.getId());
        assertThat(checkingItemDto.getOrderStatus()).isEqualTo(OrderStatus.REFUND_END);
    }

    @Test
    @DisplayName("교환 요청이 온 상품을 교환완료 처리하고, 새 상품을 출고한다.")
    void exchange_end() {
        // given
        mockingCreateInvoiceNumber();

        int orderQuantity1 = 2;
        int orderQuantity2 = 4;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity1, OrderStatus.CHECKING),
                new OrderItem(product2, orderQuantity2, OrderStatus.CHECKING)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "refund-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        // when
        ExchangeEndResultDto exchangeEndResultDto = orderStatusChangeService.exchangeEnd(orderItem1.getId());

        // then
        OrderItemDto exchangeOrderItem = exchangeEndResultDto.getExchangeOrderItem();
        assertThat(exchangeOrderItem).isNotNull();
        assertThat(exchangeOrderItem.getOrderStatus()).isEqualTo(OrderStatus.EXCHANGE_END);

        OrderDto newOrder = exchangeEndResultDto.getNewOrder();
        List<OrderItemDto> newOrderItems = newOrder.getItems();
        assertThat(newOrderItems).hasSize(1);
        OrderItemDto newOrderItem = newOrderItems.get(0);
        assertThat(newOrderItem.getOrderStatus()).isEqualTo(OrderStatus.OUTING);

        // 주문상품의 상품과 주문 수량은 동일하게 재배송해야 한다.
        assertThat(newOrderItem.getQuantity()).isEqualTo(exchangeOrderItem.getQuantity());
        assertThat(newOrderItem.getProductId()).isEqualTo(exchangeOrderItem.getProductId());

        // 주문 번호는 다르게 채번되어야 한다.
        assertThat(newOrder.getOrderCode()).isNotEqualTo(order.getOrderCode());
        assertThat(newOrder.getId()).isNotEqualTo(order.getId());
    }

    @Test
    @DisplayName("교환 물품의 재고수량이 부족하면, 교환이 불가하다.")
    void exchange_fail_with_not_enough_stock() {
        // given
        List<OrderItem> items = List.of(
                new OrderItem(product, product.getStock(), OrderStatus.CHECKING)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "refund-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem1 = savedOrder.getItems().get(0);

        // when
        assertThatThrownBy(
                () -> orderStatusChangeService.exchangeEnd(orderItem1.getId())
        ).isInstanceOf(IllegalArgumentException.class);

    }

    private void mockingIncreaseOrderAmounts(Long userId, int price) {
        doNothing().when(orderUserInterfaceService).increaseOrderAmounts(userId, price);
    }

    private void mockingCreateInvoiceNumber() {
        when(orderDeliveryInterfaceService.createInvoiceNumber(any())).thenReturn(
                new OrderDeliveryInvoiceResponse(
                        "2023120911345100901",
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getZipCode(),
                        orderDeliveryInfo.getAddress(), orderDeliveryInfo.getDetailAddress(),
                        "상품 배송처 이름 - 판매자 사업장 이름", "12033",
                        "상품 배송 주소 - 판매자 사업장 주소", "상품 배송 상세주소 - 판매자 사업장 상세주소"
                )
        );
    }
}