package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class OrderFinderTest extends IntegrationTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    OrderFinder orderFinder;
    Long userId = 10L;
    Product product;
    OrderDeliveryInfo orderDeliveryInfo;

    List<OrderItem> orderItems;

    @BeforeEach
    void beforeEach() {
        orderFinder = new OrderFinder(orderRepository, orderItemRepository);
        Long categoryId = 1L;
        Long subCategoryId = 11L;

        LocalDateTime now = LocalDateTime.now();
        product = saveProduct(
                "상품1", 1000, 200, 1.0, 10,
                categoryId, subCategoryId, 10L, now
        );

        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인", "010-1234-1234",
                "10010", "수령지주소", "수령지상세주소", "요구사항"
        );

        orderItems = List.of(
                new OrderItem(product, 20, OrderStatus.PREPARED)
        );
    }

    @Test
    @DisplayName("id로 주문 조회 테스트")
    void findOrderById() {
        // given
        Order order = orderRepository.save(
                new Order(UUID.randomUUID().toString(), userId, orderItems, orderDeliveryInfo)
        );

        // when
        Order findOrder = orderFinder.findOrderById(order.getId());

        // then
        assertThat(findOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("송장번호로 주문 조회 테스트")
    void findOrderByInvoiceNumber() {
        // given
        String invoiceNumber = "invoiceNumber1";
        Order order = orderRepository.save(
                new Order(
                        UUID.randomUUID().toString(), userId, orderItems, orderDeliveryInfo
                )
        );
        OrderItem savedOrderItem = order.getItems().get(0);
        savedOrderItem.outing(invoiceNumber);

        // when
        OrderItem orderItem = orderFinder.findOrderByInvoiceNumber(invoiceNumber);

        // then
        assertThat(order.getId()).isEqualTo(orderItem.getOrder().getId());
    }

    @Test
    @DisplayName("id로 조회 실패 테스트")
    void findByIdFailTest() {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderFinder.findOrderById(0L)
        );
    }

    @Test
    @DisplayName("송장번호로 조회 실패 테스트")
    void findByInvoiceNumberFailTest() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderFinder.findOrderByInvoiceNumber("invoiceNumber")
        );
    }
}