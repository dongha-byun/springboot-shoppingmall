package springboot.shoppingmall.order.validator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
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
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class OrderValidatorTest {

    @Autowired
    OrderValidator orderValidator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    OrderRepository orderRepository;


    @Test
    @DisplayName("주문이 완료된 주문인지 검증 - 성공")
    void order_is_end_success_test() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-2222-3333"));
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));
        Product product = productRepository.save(
                new Product(
                        "상품 1", 12000, 20, 1.0, 10, LocalDateTime.now(),
                        category, subCategory, 10L,
                        "storedFileName1", "viewFileName1", "상품 설명 입니다.",
                        "test-product-code"
                )
        );
        List<OrderItem> orderItems = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY_END));

        Delivery delivery = deliveryRepository.save(new Delivery("배송지1", "수령인1", "10010", "주소", "상세주소", "요청사항", user));

        Order savedOrder = orderRepository.save(
                new Order("test-order-code", user.getId(), orderItems,
                        delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(),
                        delivery.getDetailAddress(), delivery.getRequestMessage())
        );

        // when
        OrderItem orderItem = savedOrder.getItems().get(0);
        orderValidator.validateOrderIsEnd(orderItem.getId());

        // then
        assertThat(orderItem.getId()).isNotNull();
    }

}