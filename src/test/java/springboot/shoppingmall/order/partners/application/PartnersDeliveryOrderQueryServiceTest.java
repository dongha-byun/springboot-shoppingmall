package springboot.shoppingmall.order.partners.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.any;
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
import springboot.shoppingmall.order.application.OrderUserInterfaceService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.application.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@SpringBootTest
class PartnersDeliveryOrderQueryServiceTest extends IntegrationTest {
    @Autowired
    PartnersDeliveryOrderQueryService service;

    @MockBean
    OrderUserInterfaceService orderUserInterfaceService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Category category, subCategory;
    Product product1, product2, product3;
    Order order1, order2, order3;
    LocalDateTime orderDate = LocalDateTime.of(2023, 8, 13, 12, 0, 0);
    Long partnersId = 10L;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(new Category("식품 분류"));
        subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));

        LocalDateTime registerDate = LocalDateTime.of(2021, 8, 15, 0, 0, 0);
        product1 = saveProduct("생선1", 1000, 1.0, 10, registerDate);
        product2 = saveProduct("생선2", 1200, 1.5, 20, registerDate);
        product3 = saveProduct("생선3", 1500, 3.0, 15, registerDate);

        order1 = makeOrder("test-order-code1", 10L, product1, 3, orderDate.plusDays(1));
        order2 = makeOrder("test-order-code2", 20L, product2, 4, orderDate.plusDays(2));
        order3 = makeOrder("test-order-code3", 20L, product3, 5, orderDate.plusDays(4));
    }

    @Test
    @DisplayName("판매자가 상품이 배송중인 주문 내역을 조회한다.")
    void find_partners_delivery_orders() {
        // given
        mockingGetUserInfo();

        orderRepository.save(order1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem2 = getFirstOrderItemOf(savedOrder2);
        String invoiceNumber2 = "test-invoice-number2";
        orderItem2.outing(invoiceNumber2);
        orderItem2.delivery(LocalDateTime.of(2023, 8, 25, 12, 0, 0));

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem orderItem3 = getFirstOrderItemOf(savedOrder3);
        String invoiceNumber3 = "test-invoice-number3";
        orderItem3.outing(invoiceNumber3);
        orderItem3.delivery(LocalDateTime.of(2023, 8, 27, 12, 0, 0));

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 1, 23, 59, 59);
        List<PartnersDeliveryOrderQueryDto> orders = service.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(orders).hasSize(2)
                .extracting("orderItemId", "userId", "userName", "userTelNo", "orderStatus", "deliveryStartDate")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder2), 20L, "사용자 20", "010-2222-3320", OrderStatus.DELIVERY, LocalDateTime.of(2023, 8, 25, 12, 0, 0)),
                        tuple(getFirstOrderItemIdOf(savedOrder3), 20L, "사용자 20", "010-2222-3320", OrderStatus.DELIVERY, LocalDateTime.of(2023, 8, 27, 12, 0, 0))
                );
    }

    private OrderItem getFirstOrderItemOf(Order order) {
        return order.getItems().get(0);
    }

    private Long getFirstOrderItemIdOf(Order order) {
        return getFirstOrderItemOf(order).getId();
    }

    private void mockingGetUserInfo() {
        List<ResponseOrderUserInformation> response = Arrays.asList(
                new ResponseOrderUserInformation(10L, "사용자 10", "010-2222-3310"),
                new ResponseOrderUserInformation(20L, "사용자 20", "010-2222-3320")
        );
        when(orderUserInterfaceService.getUsersOfOrders(any())).thenReturn(response);
    }

    private Product saveProduct(String name, int price, double score, int salesVolume, LocalDateTime now) {
        return saveProduct(
                name, price, 10, score, salesVolume,
                category.getId(), subCategory.getId(), partnersId, now
        );
    }

    private Order makeOrder(String orderCode, Long userId, Product product, int quantity, LocalDateTime orderDate) {
        return new Order(
                orderCode, userId,
                List.of(new OrderItem(product, quantity, OrderStatus.PREPARED)),
                orderDate,
                new OrderDeliveryInfo(
                        "수령인1", "01234", "010-1234-1234",
                        "테스트구 테스트로", "테스트호 테스트동", "택배 보관함에 넣어주세요"
                )
        );
    }
}