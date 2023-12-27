package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional
@SpringBootTest
class OrderDeliveryInvoiceServiceTest {

    @Autowired
    OrderDeliveryInvoiceService invoiceService;

    @Autowired
    OrderRepository orderRepository;

    String invoiceNumber = "test-invoice-number";

    Product product;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    OrderDeliveryInfo orderDeliveryInfo;

    Long userId = 10L;

    @BeforeEach
    void beforeEach() {
        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령지 1", "수령인 1", "10010",
                "서울시 동작구 사당동", "101호", "도착 시 연락주세요."
        );

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(
                new Product(
                        "상품1", 1000, 2, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
    }

    @Test
    @DisplayName("배송중 상태로 변경")
    void delivery() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.PREPARED);
        orderItem.outing(invoiceNumber);
        List<OrderItem> orderItems = List.of(orderItem);
        orderRepository.save(
                new Order(
                        UUID.randomUUID().toString(), userId, orderItems, orderDeliveryInfo
                )
        );

        // when
        LocalDateTime deliveryStartDate = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        OrderItemDto deliveryItem = invoiceService.delivery(invoiceNumber, deliveryStartDate);

        // then
        assertThat(deliveryItem.getInvoiceNumber()).isEqualTo(invoiceNumber);
        assertThat(deliveryItem.getId()).isEqualTo(orderItem.getId());
        assertThat(deliveryItem.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY);
    }

    @Test
    @DisplayName("배송완료 상태로 변경 - 최종 배송장소와 배송시간도 추가로 저장한다.")
    void delivery_end() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.PREPARED);
        orderItem.outing(invoiceNumber);
        orderItem.delivery(LocalDateTime.of(2023, 5, 15, 15, 0, 0));

        List<OrderItem> orderItems = List.of(orderItem);
        orderRepository.save(
                new Order(
                        UUID.randomUUID().toString(), userId, orderItems, orderDeliveryInfo
                )
        );

        LocalDateTime deliveryCompleteDate =
                LocalDateTime.of(2023, 5, 18, 12, 0, 0);
        String deliveryPlace = "문 앞";

        // when
        OrderItemDto deliveryCompleteItem =
                invoiceService.deliveryEnd(invoiceNumber, deliveryCompleteDate, deliveryPlace);

        // then
        assertThat(deliveryCompleteItem.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_END);
        assertThat(deliveryCompleteItem.getId()).isEqualTo(orderItem.getId());
        assertThat(deliveryCompleteItem.getInvoiceNumber()).isEqualTo(invoiceNumber);
    }
}