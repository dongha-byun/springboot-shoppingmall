package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.user.domain.Delivery;

public class OrderTest {

    Product product1;
    Product product2;
    Delivery delivery;

    List<OrderItem> orderItems;
    @BeforeEach
    void beforeEach() {
        product1 = new Product(
                1L, "상품1", 12000, 22, 0.0, 0,
                LocalDateTime.now(), new Category("상위 카테고리"), new Category("하위 카테고리"),
                100L, "stored_file_name1", "view_file_name1",
                "상품 설명 입니다.", "test-product-code1"
        );
        product2 = new Product(
                2L, "상품2", 10000, 20, 3.0, 10,
                LocalDateTime.now(), new Category("상위 카테고리"), new Category("하위 카테고리"),
                101L, "stored_file_name2", "view_file_name2",
                "상품 설명 입니다. 2", "test-product-code2"
        );
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build();
        orderItems = Arrays.asList(
                new OrderItem(product1, 2, OrderStatus.READY),
                new OrderItem(product2, 2, OrderStatus.READY)
        );
    }

    @Test
    @DisplayName("다수 상품 주문 시, 총 합계가 자동 계산된다.")
    void auto_calculate_total_price() {
        // given
        int quantity = 2;
        Order order = Order.createOrder("outing-order-code", 1L, orderItems,
                delivery.getReceiverName(), delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage());

        // when
        int totalPrice = order.getTotalPrice();
        int product1_price = product1.getPrice() * quantity;
        int product2_price = product2.getPrice() * quantity;

        // then
        assertThat(totalPrice).isEqualTo(product1_price + product2_price);
    }
}
