package springboot.shoppingmall.payment.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.delivery.domain.Delivery;
import springboot.shoppingmall.delivery.domain.DeliveryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.orderhistory.domain.OrderHistoryRepository;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;

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

    @Autowired
    PayHistoryRepository payHistoryRepository;

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
                .email("test1@test.com")
                .password("test1!")
                .telNo("010-1234-1234")
                .build());
        delivery = deliveryRepository.save(Delivery.builder()
                .address("주소지 1")
                .detailAddress("상세 주소지 1")
                .nickName("배송지 1")
                .receiverName("수령인 1")
                .requestMessage("요구사항 1")
                .zipCode("10001")
                .userId(user.getId())
                .build());
        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(), delivery.getZipCode(),
                delivery.getAddress(), delivery.getDetailAddress(), delivery.getRequestMessage()
        );

        Category parent = categoryRepository.save(new Category("카테고리1"));
        Category child = categoryRepository.save(new Category("하위 카테고리1"));

        partner = providerRepository.save(
                new Provider("테스트판매처", "테스터", "테스트시 테스트구 테스트동",
                        "031-222-3121", "102-33-122333",
                        "provider_test", "provider_test1!", true)
        );
        product = productRepository.save(
                new Product("테스트상품", 12000, 300, parent, child,
                        partner.getId(), "storedFileName", "viewFileName",
                        "상품 설명 입니다.", "test-product-code")
        );

        order1 = new Order(
                UUID.randomUUID().toString(), user.getId(),
                List.of(new OrderItem(product, 2, OrderStatus.READY)),
                LocalDateTime.of(2022, 11, 5, 12, 0, 0),
                orderDeliveryInfo
        );

        order2 = new Order(
                UUID.randomUUID().toString(), user.getId(),
                List.of(new OrderItem(product, 2, OrderStatus.READY)),
                LocalDateTime.of(2023, 2, 5, 12, 0, 0),
                orderDeliveryInfo
        );

        order3 = new Order(
                UUID.randomUUID().toString(), user.getId(),
                List.of(new OrderItem(product, 2, OrderStatus.READY)),
                LocalDateTime.of(2023, 5, 5, 12, 0, 0),
                orderDeliveryInfo
        );
    }

    @Test
    @DisplayName("사용자 별 주문내역 조회")
    void orderHistoryTest(){
        // given
        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        payHistoryRepository.save(
                new PayHistory(savedOrder1.getId(), PayType.KAKAO_PAY.name(), "test-tid-order1",
                        savedOrder1.getTotalPrice())
        );
        payHistoryRepository.save(
                new PayHistory(savedOrder2.getId(), PayType.KAKAO_PAY.name(), "test-tid-order2",
                        savedOrder2.getTotalPrice())
        );
        payHistoryRepository.save(
                new PayHistory(savedOrder3.getId(), PayType.KAKAO_PAY.name(), "test-tid-order3",
                        savedOrder3.getTotalPrice())
        );

        // when
        LocalDateTime startDate =
                LocalDateTime.of(2023, 2, 1, 0, 0, 0);
        LocalDateTime endDate =
                LocalDateTime.of(2023, 6, 1, 23, 59, 59);
        List<OrderHistoryDto> orderHistories = orderHistoryRepository.queryOrderHistory(user, startDate, endDate);

        // then
        assertThat(orderHistories).hasSize(2);
        List<Long> orderItemIds = orderHistories.stream()
                .map(OrderHistoryDto::getOrderItemId)
                .collect(Collectors.toList());
        assertThat(orderItemIds).containsExactly(
                savedOrder3.getItems().get(0).getId(),
                savedOrder2.getItems().get(0).getId()
        );
    }
}