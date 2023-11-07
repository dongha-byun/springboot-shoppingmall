package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.TestOrderConfig;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.application.dto.ResponseUserOrderAmount;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Import(TestOrderConfig.class)
@Transactional
@SpringBootTest
class OrderStatusChangeServiceTest {

    @Autowired
    OrderStatusChangeService orderStatusChangeService;

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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void beforeEach() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

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
    @DisplayName("주문 취소 - 준비 중인 주문 상품에 대해서는 별도로 주문취소 처리가 가능하다.")
    void order_item_cancel() {
        // given
        int orderQuantity = 3;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product, orderQuantity, OrderStatus.READY),
                new OrderItem(product2, orderQuantity, OrderStatus.READY)
        );
        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 6, 12, 0, 0);
        Order order = new Order(
                "ready-order-code", userId, items, orderDate, orderDeliveryInfo
        );
        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();
        Long orderItemId = savedOrder.getItems().get(0).getId();

        // when
        String cancelReason = "단순변심으로 구매 취소합니다.";
        LocalDateTime cancelDate = LocalDateTime.of(2023, 5, 9, 13, 10, 12);
        OrderItemDto cancelItem = orderStatusChangeService.cancel(orderId, orderItemId, cancelDate, cancelReason);

        // then
        assertThat(cancelItem.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(cancelItem.getCancelDate()).isEqualTo(cancelDate);
        assertThat(cancelItem.getCancelReason()).isEqualTo(cancelReason);

        // 주문을 취소하면 상품 갯수를 원래대로 되돌린다.
        Product findProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(findProduct.getCount()).isEqualTo(productCount);
    }

    @Test
    @DisplayName("교환 신청 - 배송이 완료된 주문 상품에 대해 교환신청을 할 수 있다.")
    void exchange_test() {
        // 배송이 완료된 주문상품이 존재한다.
        // 해당 주문상품을 교환신청 한다.
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when

        // then
    }

    @Test
    @DisplayName("구매 확정 - 배송이 완료된 상품을 구매확정 처리 한다. 동시에 사용자의 주문횟수/주문금액을 증가시킨다.")
    void order_finish_and_user_grade_info_update() throws JsonProcessingException {
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

    private void mockingIncreaseOrderAmounts(Long userId, int price) throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(new ResponseUserOrderAmount(userId, 100, price));
        mockRestServiceServer
                .expect(
                        requestTo("/users/"+userId+"/order-amounts")
                )
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(content)
                );
    }
}