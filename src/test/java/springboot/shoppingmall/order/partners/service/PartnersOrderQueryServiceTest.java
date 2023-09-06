package springboot.shoppingmall.order.partners.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
import springboot.shoppingmall.order.partners.controller.PartnersEndOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;
import springboot.shoppingmall.utils.DateUtils;

@Transactional
@SpringBootTest
class PartnersOrderQueryServiceTest {

    @Autowired
    PartnersOrderQueryRepository queryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    User user;
    Product product1;
    Product product2;
    Product product3;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long partnersId = 10L;

    Order order1;
    Order order2;
    Order order3;


    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("주문 테스터", "order_tester", "order_tester1!", "010-2222-3333")
        );
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime now = LocalDateTime.now();
        product1 = productRepository.save(
                new Product(
                        "생선1", 1000, 10, 1.0, 10, now,
                        category, subCategory, partnersId,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "생선2", 1200, 11, 1.5, 20, now.plusDays(1),
                        category, subCategory, partnersId,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product3 = productRepository.save(
                new Product(
                        "생선3", 1500, 12, 3.0, 15, now.plusDays(2),
                        category, subCategory, partnersId,
                        "storedFileName3", "viewFileName3", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인1", "010-1234-1234", "01234",
                "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요"
        );
        order1 = Order.createOrder(
                "test-order-code1", user.getId(),
                List.of(new OrderItem(product1, 3, OrderStatus.READY)),
                orderDeliveryInfo
        );

        order2 = Order.createOrder(
                "test-order-code2", user.getId(),
                List.of(new OrderItem(product2, 4, OrderStatus.READY)),
                orderDeliveryInfo
        );

        order3 = Order.createOrder(
                "test-order-code3", user.getId(),
                List.of(new OrderItem(product3, 5, OrderStatus.READY)),
                orderDeliveryInfo
        );

        String startDateStr = DateUtils.toStringOfLocalDateTIme(now.minusMonths(3), "yyyy-MM-dd");
        String endDateStr = DateUtils.toStringOfLocalDateTIme(now, "yyyy-MM-dd");
        startDate = DateUtils.toStartDate(startDateStr);
        endDate = DateUtils.toEndDate(endDateStr);
    }

    @Test
    @DisplayName("판매자 - 준비중 및 출고중인 주문 내역 목록 조회")
    void find_partners_order_ready() {
        // given
        PartnersOrderQueryService partnersOrderQueryService = new PartnersReadyOrderQueryService(queryRepository);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        OrderItem savedOrder3Item = savedOrder3.getItems().get(0);
        String invoiceNumber = "test-invoice-number";
        savedOrder3Item.outing(invoiceNumber);

        // when
        List<PartnersOrderQueryResponse> partnersReadyOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersReadyOrders).hasSize(3);
        List<String> orderStatusNames = partnersReadyOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderStatusName)
                .collect(Collectors.toList());
        assertThat(orderStatusNames).containsExactly(
                OrderStatus.READY.getStatusName(),
                OrderStatus.READY.getStatusName(),
                OrderStatus.OUTING.getStatusName()
        );

        // 송장번호 check
        List<String> invoiceNumbers = partnersReadyOrders.stream()
                .map(PartnersOrderQueryResponse::getInvoiceNumber)
                .collect(Collectors.toList());
        assertThat(invoiceNumbers).containsExactly(
                null, null, invoiceNumber
        );
    }

    @Test
    @DisplayName("판매자 - 배송 중인 주문 내역 목록 조회")
    void find_partners_order_delivery() {
        // given
        PartnersOrderQueryService partnersOrderQueryService = new PartnersDeliveryOrderQueryService(queryRepository);

        Order savedOrder1 = orderRepository.save(order1);
        OrderItem savedOrder1Item = savedOrder1.getItems().get(0);
        String invoiceNumber1 = "test-invoice-number1";
        savedOrder1Item.outing(invoiceNumber1);
        LocalDateTime deliveryDate1 = LocalDateTime.of(2023, 5, 5, 12, 0, 0);
        savedOrder1Item.delivery(deliveryDate1);

        Order savedOrder2 = orderRepository.save(order2);
        OrderItem savedOrder2Item = savedOrder2.getItems().get(0);
        String invoiceNumber2 = "test-invoice-number2";
        savedOrder2Item.outing(invoiceNumber2);
        LocalDateTime deliveryDate2 = LocalDateTime.of(2023, 5, 5, 12, 0, 0);
        savedOrder2Item.delivery(deliveryDate2);

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem savedOrder3Item = savedOrder3.getItems().get(0);
        String invoiceNumber3 = "test-invoice-number3";
        savedOrder3Item.outing(invoiceNumber3);
        LocalDateTime deliveryDate3 = LocalDateTime.of(2023, 5, 5, 12, 0, 0);
        savedOrder3Item.delivery(deliveryDate3);

        // when
        List<PartnersOrderQueryResponse> partnersDeliveryOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersDeliveryOrders).hasSize(3);
        List<Long> ids = partnersDeliveryOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                savedOrder1Item.getId(), savedOrder2Item.getId(), savedOrder3Item.getId()
        );
    }

    @Test
    @DisplayName("판매자 - 배송완료된 주문 내역 목록 조회")
    void find_partners_order_end() {
        // given
        PartnersOrderQueryService partnersOrderQueryService = new PartnersEndOrderQueryService(queryRepository);
        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        Order savedOrder3 = orderRepository.save(order3);
        OrderItem savedOrder3Item = savedOrder3.getItems().get(0);
        String invoiceNumber3 = "test-invoice-number3";
        savedOrder3Item.outing(invoiceNumber3);

        LocalDateTime deliveryDate3 = LocalDateTime.of(2023, 5, 5, 12, 0, 0);
        savedOrder3Item.delivery(deliveryDate3);

        LocalDateTime deliveryCompleteDate = LocalDateTime.of(2023, 5, 8, 2, 0, 30);
        String deliveryPlace = "현관문 앞";
        savedOrder3Item.deliveryComplete(deliveryCompleteDate, deliveryPlace);

        // when
        List<PartnersOrderQueryResponse> partnersEndOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersEndOrders).hasSize(1);
        List<Long> ids = partnersEndOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                savedOrder3Item.getId()
        );

        List<String> deliveryPlaces = partnersEndOrders.stream()
                .map(dto -> ((PartnersEndOrderQueryResponse) dto).getDeliveryPlace())
                .collect(Collectors.toList());
        assertThat(deliveryPlaces).containsExactly(
                deliveryPlace
        );
    }
}