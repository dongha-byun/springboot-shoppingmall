package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@Transactional
@SpringBootTest
class OrderHistoryRepositoryTest {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OrderRepository orderRepository;

    User user;
    Delivery delivery;
    Product product;
    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder().userName("테스터")
                .loginId("test1")
                .password("test1!")
                .telNo("010-1234-1234")
                .build());
        delivery = deliveryRepository.save(Delivery.builder().user(user)
                .address("주소지 1")
                .detailAddress("상세 주소지 1")
                .nickName("배송지 1")
                .receiverName("수령인 1")
                .requestMessage("요구사항 1")
                .zipCode("10001")
                .build());
        Category parent = categoryRepository.save(new Category("카테고리1"));
        Category child = categoryRepository.save(new Category("하위 카테고리1"));

        product = productRepository.save(Product.builder().name("상품1")
                .category(parent)
                .subCategory(child)
                .price(10000)
                .count(22)
                .build());
    }

    @Test
    @DisplayName("사용자 별 주문내역 조회")
    void orderHistoryTest(){
        // given
        LocalDateTime now = LocalDateTime.now();
        Order order1 =
                new Order(user.getId(), product, 2, now.minusMonths(6), OrderStatus.READY, 100000
                , delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage(), null);

        Order order2 =
                new Order(user.getId(), product, 2, now.minusMonths(3), OrderStatus.READY, 100000
                , delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage(), null);

        Order order3 =
                new Order(user.getId(), product, 2, now, OrderStatus.READY, 100000
                , delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage(), null);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // when
        LocalDateTime startDate = now.minusMonths(3);
        LocalDateTime endDate = now;
        List<OrderHistoryDto> orderHistories = orderHistoryRepository.queryOrderHistory(user, startDate, endDate);

        // then
        assertThat(orderHistories).hasSize(2);
    }
}