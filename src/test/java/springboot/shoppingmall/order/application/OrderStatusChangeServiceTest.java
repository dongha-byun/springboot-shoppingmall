package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.assertThat;
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
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.handler.OrderCanceledEventHandler;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional
@SpringBootTest
class OrderStatusChangeServiceTest {

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
    ProductRepository productRepository;

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
        product = productRepository.save(
                new Product(
                        "상품 1", 22000, productCount, 1.0, 0, registerDate,
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "상품 2", 10000, productCount, 1.0, 0, registerDate,
                        category, subCategory, 10L,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.2",
                        "test-product-code2"
                )
        );
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

        when(orderDeliveryInterfaceService.createInvoiceNumber(any())).thenReturn(
                new OrderDeliveryInvoiceResponse(
                        "2023120911345100901",
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getZipCode(),
                        orderDeliveryInfo.getAddress(), orderDeliveryInfo.getDetailAddress(),
                        "상품 배송처 이름 - 판매자 사업장 이름", "12033",
                        "상품 배송 주소 - 판매자 사업장 주소", "상품 배송 상세주소 - 판매자 사업장 상세주소"
                )
        );

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

    private void mockingIncreaseOrderAmounts(Long userId, int price) {
        doNothing().when(orderUserInterfaceService).increaseOrderAmounts(userId, price);
    }
}