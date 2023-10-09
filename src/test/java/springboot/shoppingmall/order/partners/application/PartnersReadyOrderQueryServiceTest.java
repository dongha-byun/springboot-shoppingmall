package springboot.shoppingmall.order.partners.application;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
import springboot.shoppingmall.order.application.dto.ResponseOrderUserInformation;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.application.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional
@SpringBootTest
class PartnersReadyOrderQueryServiceTest {

    @Autowired
    PartnersReadyOrderQueryService service;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product1, product2, product3;
    Order order1, order2, order3;
    OrderDeliveryInfo orderDeliveryInfo;

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

        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime registerDate = LocalDateTime.of(2021, 8, 15, 0, 0, 0);
        product1 = productRepository.save(
                new Product(
                        "생선1", 1000, 10, 1.0, 10, registerDate,
                        category, subCategory, partnersId,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "생선2", 1200, 11, 1.5, 20, registerDate,
                        category, subCategory, partnersId,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product3 = productRepository.save(
                new Product(
                        "생선3", 1500, 12, 3.0, 15, registerDate,
                        category, subCategory, partnersId,
                        "storedFileName3", "viewFileName3", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인1", "01234", "010-1234-1234",
                "테스트구 테스트로", "테스트호 테스트동", "택배 보관함에 넣어주세요"
        );

        orderDate = LocalDateTime.of(2023, 8, 13, 12, 0, 0);
        order1 = getOrder("test-order-code1", 10L, product1, 3, orderDate.plusDays(1));
        order2 = getOrder("test-order-code2", 20L, product2, 4, orderDate.plusDays(2));
        order3 = getOrder("test-order-code3", 20L, product3, 5, orderDate.plusDays(4));
    }

    private Order getOrder(String orderCode, Long userId, Product product, int quantity, LocalDateTime orderDate) {
        return new Order(
                orderCode, userId,
                List.of(new OrderItem(product, quantity, OrderStatus.READY)),
                orderDate,
                orderDeliveryInfo
        );
    }

    @Test
    @DisplayName("판매자가 상품 준비중이거나 출고중인 주문 내역을 조회한다.")
    void find_partners_ready_orders() throws JsonProcessingException {
        // given
        mockingGetUserInfo();

        Order savedOrder1 = orderRepository.save(order1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem orderItem = getFirstOrderItemOf(savedOrder2);
        String invoiceNumber = "test-invoice-number";
        orderItem.outing(invoiceNumber);

        Order savedOrder3 = orderRepository.save(order3);

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 1, 23, 59, 59);
        List<PartnersReadyOrderQueryDto> partnersOrders = service.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersOrders).hasSize(3)
                .extracting("orderItemId", "userId", "userName", "userTelNo", "orderStatus")
                .containsExactly(
                        tuple(getFirstOrderItemIdOf(savedOrder1), 10L, "사용자 10", "010-2222-3310", OrderStatus.READY),
                        tuple(getFirstOrderItemIdOf(savedOrder2), 20L, "사용자 20", "010-2222-3320", OrderStatus.OUTING),
                        tuple(getFirstOrderItemIdOf(savedOrder3), 20L, "사용자 20", "010-2222-3320", OrderStatus.READY)
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
}