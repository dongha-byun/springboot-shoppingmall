package springboot.shoppingmall.order.partners.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.application.OrderItemResolutionService;
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.application.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional
@SpringBootTest
class PartnersCancelOrderQueryServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    PartnersCancelOrderQueryService service;

    @Autowired
    OrderItemResolutionService orderItemResolutionService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Category category, subCategory;
    Product product1, product2, product3;
    Order order1, order2, order3;

    LocalDateTime orderDate;
    Long partnersId = 10L;

    @Autowired
    RestTemplate restTemplate;

    MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

        category = categoryRepository.save(new Category("식품 분류"));
        subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime registerDate = LocalDateTime.of(2021, 8, 15, 0, 0, 0);

        product1 = saveProduct("생선1", 1000, 1.0, 10, registerDate);
        product2 = saveProduct("생선2", 1200, 1.5, 20, registerDate);
        product3 = saveProduct("생선3", 1500, 3.0, 15, registerDate);

        orderDate = LocalDateTime.of(2023, 8, 13, 12, 0, 0);
        order1 = getOrder("test-order-code1", 10L, this.product1, 3, orderDate.plusDays(1));
        order2 = getOrder("test-order-code2", 20L, product2, 4, orderDate.plusDays(2));
        order3 = getOrder("test-order-code3", 20L, product3, 5, orderDate.plusDays(4));
    }

    @Test
    @DisplayName("판매자가 상품 주문이 환불/교환/취소된 내역을 조회한다.")
    void find_partners_ready_orders() throws JsonProcessingException {
        // given
        mockingGetUserInfo();

        Order savedOrder1 = orderRepository.save(order1);
        OrderItem orderItem1 = getFirstOrderItemOf(savedOrder1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem2 = getFirstOrderItemOf(savedOrder2);
        String invoiceNumber2 = "test-invoice-number2";
        orderItem2.outing(invoiceNumber2);
        orderItem2.delivery(LocalDateTime.of(2023, 8, 15, 12, 0 ,0));
        orderItem2.deliveryComplete(
                LocalDateTime.of(2023, 8, 17, 12, 0 ,0),
                "현관문 앞"
        );

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem orderItem3 = getFirstOrderItemOf(savedOrder3);
        String invoiceNumber3 = "test-invoice-number3";
        orderItem3.outing(invoiceNumber3);
        orderItem3.delivery(LocalDateTime.of(2023, 8, 16, 12, 0 ,0));
        orderItem3.deliveryComplete(
                LocalDateTime.of(2023, 8, 16, 12, 0 ,0),
                "현관문 앞"
        );

        em.flush();
        em.clear();

        orderItemResolutionService.saveResolutionHistory(
                10L, orderItem1.getId(), OrderItemResolutionType.CANCEL,
                LocalDateTime.of(2023, 8, 25, 11, 0, 0),
                "주문 취소합니다."
        );
        orderItemResolutionService.saveResolutionHistory(
                20L, orderItem2.getId(), OrderItemResolutionType.REFUND,
                LocalDateTime.of(2023, 8, 23, 12, 30, 0),
                "환불합니다."
        );
        orderItemResolutionService.saveResolutionHistory(
                20L, orderItem3.getId(), OrderItemResolutionType.EXCHANGE,
                LocalDateTime.of(2023, 8, 24, 8, 0, 0),
                "교환합니다."
        );

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 1, 23, 59, 59);
        List<PartnersCancelOrderQueryDto> orders = service.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(orders).hasSize(3)
                .extracting(
                        "orderItemId", "userId", "userName", "userTelNo", "orderStatus",
                        "resolutionType", "resolutionDate", "resolutionReason"
                )
                .containsExactly(
                        tuple(
                                getFirstOrderItemIdOf(savedOrder1), 10L, "사용자 10", "010-2222-3310",
                                OrderStatus.CANCEL, OrderItemResolutionType.CANCEL,
                                LocalDateTime.of(2023, 8, 25, 11, 0, 0),
                                "주문 취소합니다."
                        ),
                        tuple(
                                getFirstOrderItemIdOf(savedOrder2), 20L, "사용자 20", "010-2222-3320",
                                OrderStatus.REFUND, OrderItemResolutionType.REFUND,
                                LocalDateTime.of(2023, 8, 23, 12, 30, 0),
                                "환불합니다."
                        ),
                        tuple(
                                getFirstOrderItemIdOf(savedOrder3), 20L, "사용자 20", "010-2222-3320",
                                OrderStatus.EXCHANGE, OrderItemResolutionType.EXCHANGE,
                                LocalDateTime.of(2023, 8, 24, 8, 0, 0),
                                "교환합니다."
                        )
                );
    }

    private OrderItem getFirstOrderItemOf(Order order) {
        return order.getItems().get(0);
    }

    private Long getFirstOrderItemIdOf(Order order) {
        return getFirstOrderItemOf(order).getId();
    }

    private void mockingGetUserInfo() throws JsonProcessingException {
        List<ResponseOrderUserInformation> response = Arrays.asList(
                new ResponseOrderUserInformation(10L, "사용자 10", "010-2222-3310"),
                new ResponseOrderUserInformation(20L, "사용자 20", "010-2222-3320")
        );
        String responseContent = objectMapper.writeValueAsString(response);
        mockRestServiceServer
                .expect(
                        requestTo("/orders/users/basic-info")
                )
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(responseContent)
                );
    }

    private Order getOrder(String orderCode, Long userId, Product product, int quantity, LocalDateTime orderDate) {
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

    private Product saveProduct(String name, int price, double score, int salesVolume, LocalDateTime now) {
        String storedFileName = "stored-file-name-" + name;
        String viewFileName = "view-file-name-" + name;
        return productRepository.save(
                new Product(
                        name, price, 10, score, salesVolume, now,
                        category, subCategory, partnersId,
                        storedFileName, viewFileName, "상품 설명 입니다.",
                        "product-code"
                )
        );
    }
}