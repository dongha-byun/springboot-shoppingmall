package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class OrderFinderTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    UserRepository userRepository;

    OrderFinder orderFinder;
    User user;
    Product product;
    Delivery delivery;

    List<OrderItem> orderItems;

    @BeforeEach
    void beforeEach() {
        orderFinder = new OrderFinder(orderRepository, orderItemRepository);
        user = userRepository.save(new User("테스트유저", "testUser", "testUser!", "010-1234-1234"));
        Category category = categoryRepository.save(new Category("의류"));
        Category subCategory = categoryRepository.save(new Category("바지").changeParent(category));
        LocalDateTime now = LocalDateTime.now();
        product = productRepository.save(
                new Product(
                        "상품1", 1000, 200, 1.0, 10, now,
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        delivery = deliveryRepository.save(
                new Delivery("닉네임", "수령인", "010-1234-1234",
                        "10010", "수령지주소", "수령지상세주소", "요구사항", user)
        );

        orderItems = List.of(
                new OrderItem(product, 20, OrderStatus.READY)
        );
    }

    @Test
    @DisplayName("id로 주문 조회 테스트")
    void findOrderById() {
        // given
        Order order = orderRepository.save(
                new Order(UUID.randomUUID().toString(), user.getId(), orderItems,
                        delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                        delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress(),
                        delivery.getRequestMessage()
                )
        );

        // when
        Order findOrder = orderFinder.findOrderById(order.getId());

        // then
        assertThat(findOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("송장번호로 주문 조회 테스트")
    void findOrderByInvoiceNumber() {
        // given
        String invoiceNumber = "invoiceNumber1";
        Order order = orderRepository.save(
                new Order(
                        UUID.randomUUID().toString(), user.getId(), orderItems,
                        delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                        delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress(),
                        delivery.getRequestMessage()
                )
        );
        OrderItem savedOrderItem = order.getItems().get(0);
        savedOrderItem.outing(invoiceNumber);

        // when
        OrderItem orderItem = orderFinder.findOrderByInvoiceNumber(invoiceNumber);

        // then
        assertThat(order.getId()).isEqualTo(orderItem.getOrder().getId());
    }

    @Test
    @DisplayName("id로 조회 실패 테스트")
    void findByIdFailTest() {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderFinder.findOrderById(0L)
        );
    }

    @Test
    @DisplayName("송장번호로 조회 실패 테스트")
    void findByInvoiceNumberFailTest() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderFinder.findOrderByInvoiceNumber("invoiceNumber")
        );
    }
}