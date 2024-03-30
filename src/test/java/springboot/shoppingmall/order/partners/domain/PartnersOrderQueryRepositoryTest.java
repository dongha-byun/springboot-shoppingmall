package springboot.shoppingmall.order.partners.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.application.OrderItemResolutionService;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistory;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistoryRepository;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class PartnersOrderQueryRepositoryTest extends IntegrationTest{

    @Autowired
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemResolutionService orderItemResolutionService;

    @Autowired
    OrderItemResolutionHistoryRepository orderItemResolutionHistoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartnersOrderQueryRepository partnersOrderQueryRepository;

    Product product1, product2, product3;

    Order order1, order2, order3;

    OrderDeliveryInfo orderDeliveryInfo;

    LocalDateTime orderDate;
    Long userId = 100L;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime registerDate = LocalDateTime.of(2021, 8, 15, 0, 0, 0);

        product1 = saveProduct(
                "생선1", 1000, 10, 1.0, 10,
                category.getId(), subCategory.getId(), 1L, registerDate
        );
        product2 = saveProduct(
                "생선2", 1200, 11, 1.5, 20,
                category.getId(), subCategory.getId(), 1L, registerDate
        );
        product3 = saveProduct(
                "생선3", 1500, 12, 3.0, 15,
                category.getId(), subCategory.getId(), 1L, registerDate
        );

        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인1", "01234", "010-1234-1234",
                "테스트구 테스트로", "테스트호 테스트동", "택배 보관함에 넣어주세요"
        );

        orderDate = LocalDateTime.of(2023, 8, 13, 12, 0, 0);
        order1 = getOrder("test-order-code1", product1, 3, orderDate.plusDays(1));
        order2 = getOrder("test-order-code2", product2, 4, orderDate.plusDays(2));
        order3 = getOrder("test-order-code3", product3, 5, orderDate.plusDays(4));
    }

    private Order getOrder(String orderCode, Product product, int quantity, LocalDateTime orderDate) {
        return new Order(
                orderCode, userId,
                List.of(new OrderItem(product, quantity, OrderStatus.PREPARED)),
                orderDate,
                orderDeliveryInfo
        );
    }

    @Test
    @DisplayName("판매자가 준비 중인 주문 내역들을 조회한다.")
    void find_partner_order_ready() {
        // given
        Order savedOrder1 = orderRepository.save(order1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem = getFirstOrderItemOf(savedOrder2);
        String invoiceNumber = "test-invoice-number";
        orderItem.outing(invoiceNumber);

        Order savedOrder3 = orderRepository.save(order3);

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 13, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 31, 0, 0, 0);
        List<PartnersReadyOrderQueryDto> readyOrders =
                partnersOrderQueryRepository.findPartnersReadyOrders(1L, startDate, endDate);

        // then
        assertThat(readyOrders).hasSize(3)
                .extracting("orderItemId", "userId", "orderStatus", "receiverName", "invoiceNumber")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder1), 100L, OrderStatus.PREPARED, "수령인1", null),
                        tuple(getFirstOrderItemIdOf(savedOrder2), 100L, OrderStatus.OUTING, "수령인1", "test-invoice-number"),
                        tuple(getFirstOrderItemIdOf(savedOrder3), 100L, OrderStatus.PREPARED, "수령인1", null)
                );
    }

    @Test
    @DisplayName("판매자가 배송중인 주문 내역들을 조회한다.")
    void find_partners_order_delivery() {
        // given
        Order savedOrder1 = orderRepository.save(order1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem2 = getFirstOrderItemOf(savedOrder2);
        orderItem2.outing("test-invoice-number-2");
        orderItem2.delivery(LocalDateTime.of(2023, 8, 20, 12, 30, 0));

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem orderItem3 = getFirstOrderItemOf(savedOrder3);
        orderItem3.outing("test-invoice-number-3");
        orderItem3.delivery(LocalDateTime.of(2023, 8, 21, 12, 30, 0));

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 13, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 31, 0, 0, 0);
        List<PartnersDeliveryOrderQueryDto> deliveryOrders =
                partnersOrderQueryRepository.findPartnersDeliveryOrders(1L, startDate, endDate);

        // then
        assertThat(deliveryOrders).hasSize(2)
                .extracting("orderItemId", "orderStatus", "receiverName", "invoiceNumber")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder2), OrderStatus.DELIVERY, "수령인1", "test-invoice-number-2"),
                        tuple(getFirstOrderItemIdOf(savedOrder3), OrderStatus.DELIVERY, "수령인1", "test-invoice-number-3")
                );
    }

    @Test
    @DisplayName("판매자가 배송이 완료된 주문 내역들을 조회한다.")
    void find_partners_end_orders() {
        // given
        Order savedOrder1 = orderRepository.save(order1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem2 = getFirstOrderItemOf(savedOrder2);
        orderItem2.outing("test-invoice-number-2");
        orderItem2.delivery(LocalDateTime.of(2023, 8, 20, 12, 30, 0));
        orderItem2.deliveryComplete(LocalDateTime.of(2023, 8, 22, 12, 30, 0), "현관문 앞");

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem orderItem3 = getFirstOrderItemOf(savedOrder3);
        orderItem3.outing("test-invoice-number-3");
        orderItem3.delivery(LocalDateTime.of(2023, 8, 21, 12, 30, 0));
        orderItem3.deliveryComplete(LocalDateTime.of(2023, 8, 23, 11, 11, 0), "경비실");

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 13, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 31, 0, 0, 0);
        List<PartnersEndOrderQueryDto> endOrders =
                partnersOrderQueryRepository.findPartnersEndOrders(1L, startDate, endDate);

        // then
        assertThat(endOrders).hasSize(2)
                .extracting("orderItemId", "orderStatus", "deliveryCompleteDate", "deliveryPlace")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder2), OrderStatus.DELIVERY_END, LocalDateTime.of(2023, 8, 22, 12, 30, 0), "현관문 앞"),
                        tuple(getFirstOrderItemIdOf(savedOrder3), OrderStatus.DELIVERY_END, LocalDateTime.of(2023, 8, 23, 11, 11, 0), "경비실")
                );
    }

    @Test
    @DisplayName("판매자가 주문의 취소/환불/교환 된 주문 내역들을 조회한다.")
    void find_partners_cancel_orders() {
        // given
        Order savedOrder1 = orderRepository.save(order1);
        OrderItem orderItem1 = getFirstOrderItemOf(savedOrder1);
        orderItem1.cancel();

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem2 = getFirstOrderItemOf(savedOrder2);
        orderItem2.outing("test-invoice-number-2");
        orderItem2.delivery(LocalDateTime.of(2023, 8, 20, 12, 30, 0));
        orderItem2.deliveryComplete(LocalDateTime.of(2023, 8, 22, 12, 30, 0), "현관문 앞");
        orderItem2.refund();

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem orderItem3 = getFirstOrderItemOf(savedOrder3);
        orderItem3.outing("test-invoice-number-3");
        orderItem3.delivery(LocalDateTime.of(2023, 8, 21, 12, 30, 0));
        orderItem3.deliveryComplete(LocalDateTime.of(2023, 8, 23, 11, 11, 0), "경비실");
        orderItem3.exchange();

        em.flush();
        em.clear();

        orderItemResolutionHistoryRepository.save(
                new OrderItemResolutionHistory(
                        orderItem1, OrderItemResolutionType.CANCEL,
                        LocalDateTime.of(2023, 8, 25, 11, 0, 0),
                        "배송이 너무 늦어서 그냥 취소합니다."
                )
        );
        orderItemResolutionHistoryRepository.save(
                new OrderItemResolutionHistory(
                        orderItem2, OrderItemResolutionType.REFUND,
                        LocalDateTime.of(2023, 8, 23, 12, 30, 0),
                        "생각했던 색상이 아니에요."
                )
        );
        orderItemResolutionHistoryRepository.save(
                new OrderItemResolutionHistory(
                        orderItem3, OrderItemResolutionType.EXCHANGE,
                        LocalDateTime.of(2023, 8, 24, 8, 0, 0),
                        "사이즈가 좀 커요."
                )
        );

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 13, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 31, 0, 0, 0);
        List<PartnersCancelOrderQueryDto> endOrders =
                partnersOrderQueryRepository.findPartnersCancelOrders(1L, startDate, endDate);

        // then
        assertThat(endOrders).hasSize(3)
                .extracting("orderItemId", "orderStatus", "invoiceNumber",
                        "resolutionType", "resolutionDate", "resolutionReason")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder1), OrderStatus.CANCEL, null,
                                OrderItemResolutionType.CANCEL,
                                LocalDateTime.of(2023, 8, 25, 11, 0, 0),
                                "배송이 너무 늦어서 그냥 취소합니다."),
                        tuple(getFirstOrderItemIdOf(savedOrder2), OrderStatus.REFUND, "test-invoice-number-2",
                                OrderItemResolutionType.REFUND,
                                LocalDateTime.of(2023, 8, 23, 12, 30, 0),
                                "생각했던 색상이 아니에요."),
                        tuple(getFirstOrderItemIdOf(savedOrder3), OrderStatus.EXCHANGE, "test-invoice-number-3",
                                OrderItemResolutionType.EXCHANGE,
                                LocalDateTime.of(2023, 8, 24, 8, 0, 0),
                                "사이즈가 좀 커요.")
                );
    }

    private OrderItem getFirstOrderItemOf(Order order) {
        return order.getItems().get(0);
    }

    private Long getFirstOrderItemIdOf(Order order) {
        return getFirstOrderItemOf(order).getId();
    }
}