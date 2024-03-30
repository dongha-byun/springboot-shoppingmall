package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.delivery.domain.Delivery;

public class OrderTest {

    Product product1;
    Product product2;
    Delivery delivery;

    List<OrderItem> orderItems;
    @BeforeEach
    void beforeEach() {
        Long categoryId = 1L;
        Long subCategoryId = 11L;

        product1 = new Product(
                1L, "상품1", 12000, 22, 0.0, 0,
                LocalDateTime.now(), categoryId, subCategoryId,
                100L, "stored_file_name1", "view_file_name1",
                "상품 설명 입니다.", "test-product-code1"
        );
        product2 = new Product(
                2L, "상품2", 10000, 20, 3.0, 10,
                LocalDateTime.now(), categoryId, subCategoryId,
                101L, "stored_file_name2", "view_file_name2",
                "상품 설명 입니다. 2", "test-product-code2"
        );
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build();
        orderItems = Arrays.asList(
                new OrderItem(product1, 2, OrderStatus.PREPARED),
                new OrderItem(product2, 2, OrderStatus.PREPARED)
        );
    }

    @Test
    @DisplayName("다수 상품 주문 시, 총 합계가 자동 계산된다.")
    void auto_calculate_total_price() {
        // given
        int quantity = 2;
        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage()
        );
        Order order = Order.createOrder("outing-order-code", 1L, orderItems, orderDeliveryInfo);

        // when
        int totalPrice = order.getTotalPrice();
        int product1_price = product1.getPrice() * quantity;
        int product2_price = product2.getPrice() * quantity;

        // then
        assertThat(totalPrice).isEqualTo(product1_price + product2_price);
    }

    @Test
    @DisplayName("회원 등급 할인 적용 시, 각 상품의 할인 금액이 적용된다.")
    void user_grade_discount_order_items() {
        // given
        OrderDeliveryInfo orderDeliveryInfo = new OrderDeliveryInfo(
                delivery.getReceiverName(), delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(), delivery.getAddress(),
                delivery.getDetailAddress(), delivery.getRequestMessage()
        );
        Order order = Order.createOrder("outing-order-code", 1L, orderItems, orderDeliveryInfo);

        // when
        int discountRate = 3;
        order.gradeDiscount(discountRate);

        // then
        List<Integer> gradeDiscountAmounts = order.getItems().stream()
                .map(OrderItem::getGradeDiscountAmount)
                .collect(Collectors.toList());

        assertThat(gradeDiscountAmounts).containsExactly(
                orderItems.get(0).totalPrice() * discountRate / 100,
                orderItems.get(1).totalPrice() * discountRate / 100
        );
    }
}
