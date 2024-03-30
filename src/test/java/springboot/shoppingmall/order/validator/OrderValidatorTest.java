package springboot.shoppingmall.order.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.product.domain.Product;

class OrderValidatorTest extends IntegrationTest {

    @Autowired
    OrderValidator orderValidator;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrderRepository orderRepository;


    @Test
    @DisplayName("주문이 완료된 주문인지 검증 - 성공")
    void order_is_end_success_test() {
        // given
        Long userId = 10L;
        Category category = categoryRepository.save(new Category("상위 카테고리"));
        Category subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));

        Product product = saveProduct("상품 1", 12000, 20, 1.0, 10,
                category.getId(), subCategory.getId(), 10L, LocalDateTime.now());

        List<OrderItem> orderItems = List.of(new OrderItem(product, 2, OrderStatus.DELIVERY_END));

        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인1", "010-1234-1234",
                "10010", "주소", "상세주소", "요청사항"
        );

        Order savedOrder = orderRepository.save(
                new Order("test-order-code", userId, orderItems, orderDeliveryInfo)
        );

        // when
        OrderItem orderItem = savedOrder.getItems().get(0);
        orderValidator.validateOrderIsEnd(orderItem.getId());

        // then
        assertThat(orderItem.getId()).isNotNull();
    }

}