package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.TestOrderConfig;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;


@Import(TestOrderConfig.class)
@Transactional
@SpringBootTest
class OrderStatusChangeServiceTest {

    @Autowired
    OrderStatusChangeService orderStatusChangeService;

    @MockBean
    OrderUserInterfaceService orderUserInterfaceService;

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
    @DisplayName("구매 확정 - 배송이 완료된 상품을 구매확정 처리 한다. 동시에 사용자의 주문횟수/주문금액을 증가시킨다.")
    void order_finish_and_user_grade_info_update() {
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
        orderStatusChangeService.finish(savedOrder.getId(), orderItem1.getId());

        // then
        assertThat(orderItem1.getOrderStatus()).isEqualTo(OrderStatus.FINISH);

        // 판매수량 증가
        Product orderItem1Product = orderItem1.getProduct();
        assertThat(orderItem1Product.getId()).isEqualTo(product.getId());
        assertThat(product.getSalesVolume()).isEqualTo(orderQuantity1);
    }

    private void mockingIncreaseOrderAmounts(Long userId, int price) {
        doNothing().when(orderUserInterfaceService).increaseOrderAmounts(userId, price);
    }
}