package springboot.shoppingmall.order.partners.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.utils.DateUtils;

@Slf4j
@Transactional
@SpringBootTest
class PartnersOrderQueryRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartnersOrderQueryRepository partnersOrderQueryRepository;

    User user;
    Product product1;
    Product product2;
    Product product3;
    LocalDateTime startDate;
    LocalDateTime endDate;
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
                        category, subCategory, 1L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product2 = productRepository.save(
                new Product(
                        "생선2", 1200, 11, 1.5, 20, now.plusDays(1),
                        category, subCategory, 1L,
                        "storedFileName2", "viewFileName2", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        product3 = productRepository.save(
                new Product(
                        "생선3", 1500, 12, 3.0, 15, now.plusDays(2),
                        category, subCategory, 1L,
                        "storedFileName3", "viewFileName3", "상품 설명 입니다.",
                        "test-product-code"
                )
        );

        String startDateStr = DateUtils.toStringOfLocalDateTIme(now.minusMonths(3), "yyyy-MM-dd");
        String endDateStr = DateUtils.toStringOfLocalDateTIme(now, "yyyy-MM-dd");
        startDate = DateUtils.toStartDate(startDateStr);
        endDate = DateUtils.toEndDate(endDateStr);
    }

    @Test
    @DisplayName("판매자 - 준비 중인 주문 내역 목록 조회")
    void find_partner_order_ready() {
        // given
        Order order1 = orderRepository.save(
                Order.createOrder("test-order-code1", user.getId(), product1, 3, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );
        Order order2 = orderRepository.save(
                Order.createOrder("test-order-code2", user.getId(), product2, 4, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );
        Order order3 = orderRepository.save(
                Order.createOrder("test-order-code3", user.getId(), product3, 5, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );
        order2.outing();

        // when
        List<PartnersReadyOrderQueryDto> readyOrders = partnersOrderQueryRepository.findPartnersReadyOrders(
                1L, startDate, endDate);

        // then
        assertThat(readyOrders).hasSize(3);

        List<Long> ids = readyOrders.stream()
                .map(PartnersReadyOrderQueryDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                order1.getId(), order2.getId(), order3.getId()
        );

        List<String> productNames = readyOrders.stream()
                .map(PartnersReadyOrderQueryDto::getProductName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(
                product1.getName(), product2.getName(), product3.getName()
        );

        List<String> userNames = readyOrders.stream()
                .map(PartnersReadyOrderQueryDto::getUserName)
                .collect(Collectors.toList());
        assertThat(userNames).containsExactly(
                user.getUserName(), user.getUserName(), user.getUserName()
        );

        List<String> userTelNo = readyOrders.stream()
                .map(PartnersReadyOrderQueryDto::getUserTelNo)
                .collect(Collectors.toList());
        assertThat(userTelNo).containsExactly(
                user.telNo(), user.telNo(), user.telNo()
        );

        List<OrderStatus> orderStatuses = readyOrders.stream()
                .map(PartnersReadyOrderQueryDto::getOrderStatus)
                .collect(Collectors.toList());
        assertThat(orderStatuses).containsExactly(
                OrderStatus.READY, OrderStatus.OUTING, OrderStatus.READY
        );
    }

}