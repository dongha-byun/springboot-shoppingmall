package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
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
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class OrderDeliveryInvoiceServiceTest {

    @Autowired
    OrderDeliveryInvoiceService invoiceService;

    @Autowired
    OrderRepository orderRepository;

    String invoiceNumber = "test-invoice-number";

    User user;
    Product product;
    Delivery delivery;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build();
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build();

        user.addDelivery(delivery);
        userRepository.save(user);

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(
                new Product(
                        "상품1", 1000, 2, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
    }

    @Test
    @DisplayName("배송중 상태로 변경")
    void delivery_test() {
        // given
        Order outingOrder = 특정_주문상태_데이터_생성(OrderStatus.OUTING);

        // when
        OrderResponse order = invoiceService.delivery(invoiceNumber);

        // then
        assertThat(order.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY.getStatusName());
    }

    @Test
    @DisplayName("배송완료 상태로 변경")
    void delivery_end_test() {
        // given
        Order outingOrder = 특정_주문상태_데이터_생성(OrderStatus.DELIVERY);

        // when
        OrderResponse order = invoiceService.deliveryEnd(invoiceNumber);

        // then
        assertThat(order.getOrderStatusName()).isEqualTo(OrderStatus.DELIVERY_END.getStatusName());
    }

    private Order 특정_주문상태_데이터_생성(OrderStatus status) {
        return orderRepository.save(new Order(UUID.randomUUID().toString(),
                user.getId(), product, 2, LocalDateTime.now()
                , status, product.getPrice() * 2
                , delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage(), invoiceNumber));
    }
}