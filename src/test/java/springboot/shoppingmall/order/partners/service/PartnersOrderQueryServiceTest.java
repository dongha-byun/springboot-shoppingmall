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
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
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

        order1 = Order.createOrder(
                "test-order-code1", user.getId(), List.of(new OrderItem(product1, 3, OrderStatus.READY)),
                "수령인1", "01234",
                "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요"
        );

        order2 = Order.createOrder(
                "test-order-code2", user.getId(), List.of(new OrderItem(product2, 4, OrderStatus.READY)),
                "수령인1", "01234",
                "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요"
        );

        order3 = Order.createOrder(
                "test-order-code3", user.getId(), List.of(new OrderItem(product3, 5, OrderStatus.READY)),
                "수령인1", "01234",
                "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요"
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

        // when
        List<PartnersOrderQueryResponse> partnersReadyOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersReadyOrders).hasSize(3);
        List<Long> ids = partnersReadyOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                savedOrder1.getId(), savedOrder2.getId(), savedOrder3.getId()
        );

        // 송장번호 check

    }

    @Test
    @DisplayName("판매자 - 배송 중인 주문 내역 목록 조회")
    void find_partners_order_delivery() {
        // given
        PartnersOrderQueryService partnersOrderQueryService = new PartnersDeliveryOrderQueryService(queryRepository);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        // when
        List<PartnersOrderQueryResponse> partnersDeliveryOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersDeliveryOrders).hasSize(3);
        List<Long> ids = partnersDeliveryOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                savedOrder1.getItems().get(0).getId(),
                savedOrder2.getItems().get(0).getId(),
                savedOrder3.getItems().get(0).getId()
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
        LocalDateTime deliveryDate = LocalDateTime.of(2023, 5, 8, 2, 0, 30);
        String deliveryPlace = "현관문 앞";

        // when
        List<PartnersOrderQueryResponse> partnersEndOrders =
                partnersOrderQueryService.findPartnersOrders(partnersId, startDate, endDate);

        // then
        assertThat(partnersEndOrders).hasSize(2);

        List<Long> ids = partnersEndOrders.stream()
                .map(PartnersOrderQueryResponse::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                savedOrder1.getId(), savedOrder2.getId()
        );
    }
}