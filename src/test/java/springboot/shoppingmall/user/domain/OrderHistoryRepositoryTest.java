package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
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
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@Transactional
@SpringBootTest
class OrderHistoryRepositoryTest {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    ProviderRepository providerRepository;
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
    Provider partner;

    Order order1;
    Order order2;
    Order order3;

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

        partner = providerRepository.save(
                new Provider("테스트판매처", "테스터", "테스트시 테스트구 테스트동",
                        "031-222-3121", "102-33-122333",
                        "provider_test", "provider_test1!", true)
        );
        product = productRepository.save(
                new Product("테스트상품", 12000, 300, parent, child,
                        partner.getId(), "storedFileName", "viewFileName", "상품 설명 입니다.",
                        "test-product-code")
        );

        List<OrderItem> orderItems = List.of(new OrderItem(product, 2));

        order1 = new Order(
                UUID.randomUUID().toString(), user.getId(), orderItems,
                LocalDateTime.of(2022, 11, 5, 12, 0, 0),
                OrderStatus.READY, delivery.getReceiverName(), delivery.getZipCode(),
                delivery.getAddress(), delivery.getDetailAddress(), delivery.getRequestMessage()
        );

        order2 = new Order(
                UUID.randomUUID().toString(), user.getId(), orderItems,
                LocalDateTime.of(2023, 2, 5, 12, 0, 0),
                OrderStatus.READY, delivery.getReceiverName(), delivery.getZipCode(),
                delivery.getAddress(), delivery.getDetailAddress(), delivery.getRequestMessage()
        );

        order3 = new Order(
                UUID.randomUUID().toString(), user.getId(), orderItems,
                LocalDateTime.of(2023, 5, 5, 12, 0, 0),
                OrderStatus.READY, delivery.getReceiverName(), delivery.getZipCode(),
                delivery.getAddress(), delivery.getDetailAddress(), delivery.getRequestMessage()
        );
    }

    @Test
    @DisplayName("사용자 별 주문내역 조회")
    void orderHistoryTest(){
        // given
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // when
        LocalDateTime startDate = LocalDateTime.of(2023, 2, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        List<OrderHistoryDto> orderHistories = orderHistoryRepository.queryOrderHistory(user, startDate, endDate);

        // then
        assertThat(orderHistories).hasSize(2);
    }
}