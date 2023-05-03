package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.user.domain.Delivery;

public class OrderTest {

    Product product;
    Delivery delivery;

    @BeforeEach
    void beforeEach() {
        product = new Product(
                1L, "상품1", 12000, 22, 0.0, 0,
                LocalDateTime.now(), new Category("상위 카테고리"), new Category("하위 카테고리"),
                100L, "상품 설명 입니다."
        );
        delivery = Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build();
    }

    @Test
    @DisplayName("1. 준비중 주문 - 주문이 처음 생성되면 준비중 상태로 생성된다.")
    void ready_order() {
        // given

        // when
        Order order = Order.createOrder(1L, product, 2, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.READY);
    }

    @Test
    @DisplayName("2. 상품 출고 - 준비중인 주문이 출고중 상태가 된다.")
    void outing_order() {
        // given
        Order order = Order.createOrder(1L, product, 2, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());

        // when
        order.outing();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.OUTING);

    }

    @Test
    @DisplayName("3. 구매확정 - 배송완료된 주문을 구매확정 처리한다.")
    void finish_order() {
        // given
        Order order = new Order(1L, product, 2, OrderStatus.DELIVERY_END, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());

        // when
        order.finish();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FINISH);
    }

    @Test
    @DisplayName("4. 환불신청 - 배송이 완료된 주문에 대해 환불을 신청할 수 있다.")
    void refund_order() {
        // given
        Order order = new Order(1L, product, 2, OrderStatus.DELIVERY_END, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        String refundReason = "환불 요청 합니다. 배송이 잘못왔어요.";

        // when
        order.refund(refundReason);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.REFUND);
        assertThat(order.getRefundReason()).isEqualTo(refundReason);
    }

    @Test
    @DisplayName("4-1. 환불신청 오류 - 환불사유가 없는 경우, 환불신청이 불가하다.")
    void refund_order_fail() {
        // given
        Order order = new Order(1L, product, 2, OrderStatus.DELIVERY_END, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        String emptyReason = "";

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> order.refund(emptyReason)
        );
    }

    @Test
    @DisplayName("5. 교환신청 - 배송이 완료된 주문에 대해 교환을 신청할 수 있다.")
    void exchange_order() {
        // given
        Order order = new Order(1L, product, 2, OrderStatus.DELIVERY_END, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        String exchangeReason = "교환 신청 합니다. 사이즈가 안맞아요.";

        // when
        order.exchange(exchangeReason);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.EXCHANGE);
        assertThat(order.getExchangeReason()).isEqualTo(exchangeReason);
    }

    @Test
    @DisplayName("5-1. 교환신청 오류 - 교환사유가 없는 경우, 교환신청이 불가하다.")
    void exchange_order_fail() {
        // given
        Order order = new Order(1L, product, 2, OrderStatus.DELIVERY_END, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        String emptyReason = "";

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> order.exchange(emptyReason)
        );
    }
}
