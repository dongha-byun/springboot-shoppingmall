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
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.utils.DateUtils;

@Transactional
@SpringBootTest
class PartnersOrderQueryServiceTest {

    @Autowired
    PartnersOrderQueryService partnersOrderQueryService;

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

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("주문 테스터", "order_tester", "order_tester1!", "010-2222-3333")
        );
        Category category = categoryRepository.save(new Category("식품 분류"));
        Category subCategory = categoryRepository.save(new Category("생선 분류").changeParent(category));
        LocalDateTime now = LocalDateTime.now();
        product1 = productRepository.save(
                new Product("생선1", 1000, 10, 1.0, 10, now, category, subCategory, partnersId));
        product2 = productRepository.save(
                new Product("생선2", 1200, 11, 1.5, 20, now.plusDays(1), category, subCategory, partnersId));
        product3 = productRepository.save(
                new Product("생선3", 1500, 12, 3.0, 15, now.plusDays(2), category, subCategory, partnersId));

        String startDateStr = DateUtils.toStringOfLocalDateTIme(now.minusMonths(3), "yyyy-MM-dd");
        String endDateStr = DateUtils.toStringOfLocalDateTIme(now, "yyyy-MM-dd");
        startDate = DateUtils.toStartDate(startDateStr);
        endDate = DateUtils.toEndDate(endDateStr);
    }

    @Test
    @DisplayName("판매자 - 준비 중인 주문 내역 목록 조회")
    void find_partners_order_ready() {
        // given
        Order order1 = orderRepository.save(
                Order.createOrder(user.getId(), product1, 3, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );
        Order order2 = orderRepository.save(
                Order.createOrder(user.getId(), product2, 4, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );
        Order order3 = orderRepository.save(
                Order.createOrder(user.getId(), product3, 5, "수령인1", "01234",
                        "서울시 테스트구 테스트동", "임시아파트 테스트동", "택배 보관함에 넣어주세요")
        );

        // when
        List<PartnersOrderQueryDto> partnersOrders = partnersOrderQueryService.findPartnersOrder(partnersId,
                OrderStatus.READY, startDate, endDate);

        // then
        assertThat(partnersOrders).hasSize(3);
        List<Long> ids = partnersOrders.stream()
                .map(PartnersOrderQueryDto::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                order1.getId(), order2.getId(), order3.getId()
        );
    }

}