package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.order.exception.OverQuantityException;
import springboot.shoppingmall.product.domain.Product;

class OrderItemTest {

    Category category;
    Category subCategory;
    Product product;
    int quantity = 10;
    @BeforeEach
    void beforeEach() {
        category = new Category("상위 카테고리");
        subCategory = new Category("하위 카테고리").changeParent(category);

        product = new Product(
                "상품1", 10000, quantity, category, subCategory, 10L,
                "stored-file-name", "view-file-name", "상품 상세 정보",
                "test-product-code"
        );
    }

    @Test
    @DisplayName("1-1. 주문 시, 상품의 재고 수량을 주문 수량 만큼 감소시킨다.")
    void ready_test() {
        // given
        int orderQuantity = 2;

        // when
        OrderItem orderItem = new OrderItem(product, orderQuantity, OrderStatus.READY);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.READY);
        assertThat(orderItem.getQuantity()).isEqualTo(orderQuantity);
        assertThat(product.getCount()).isEqualTo(quantity - orderQuantity);
    }

    @Test
    @DisplayName("1-2. 상품의 재고수량 보다 많은 양이 주문되는 경우, 주문에 실패한다.")
    void ready_fail_with_over_quantity() {
        // given
        int orderQuantity = product.getCount() + 1;

        // when & then
        assertThatThrownBy(
                () -> OrderItem.createOrderItem(product, orderQuantity)
        ).isInstanceOf(OverQuantityException.class);
    }

    @Test
    @DisplayName("1-3. 구매 수량은 1개 이상이어야 한다.")
    void ready_fail_with_zero_quantity() {
        // given
        int orderQuantity = 0;

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new OrderItem(product, orderQuantity, OrderStatus.READY)
        );
    }


    @Test
    @DisplayName("2-1. 상품 출고 - 준비 중인 주문 상품을 출고한다. 이 때 송장번호를 저장한다.")
    void outing_test() {
        // given
        String invoiceNumber = "test-invoice-number";
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.READY);

        // when
        orderItem.outing(invoiceNumber);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.OUTING);
        assertThat(orderItem.getInvoiceNumber()).isEqualTo(invoiceNumber);
    }

    @Test
    @DisplayName("2-2. 준비 중인 상태가 아니면 출고중 처리가 불가하다.")
    void outing_fail_with_not_ready() {
        // given
        String invoiceNumber = "test-invoice-number";
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.FINISH);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.outing(invoiceNumber)
        );
    }

    @Test
    @DisplayName("2-3. 송장 번호가 빈 값이면 출고중 처리가 불가하다.")
    void outing_fail_with_no_invoice_number() {
        // given
        String invoiceNumber = "";
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.READY);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.outing(invoiceNumber)
        );
    }

    @Test
    @DisplayName("3-1. 상품 배송중 - 출고된 상품이 배송을 시작하면 배송중 으로 처리한다.")
    void delivery_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.OUTING);

        // when
        LocalDateTime deliveryStartDate = LocalDateTime.of(2023, 5, 19, 15, 31, 11);
        orderItem.delivery(deliveryStartDate);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY);
        assertThat(orderItem.getDeliveryStartDate()).isEqualTo(deliveryStartDate);
    }

    @Test
    @DisplayName("3-2. 출고된 상품이 아니면 배송중으로 처리할 수 없다.")
    void delivery_fail_with_not_outing() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.READY);

        // when & then
        LocalDateTime deliveryStartDate = LocalDateTime.of(2023, 5, 19, 15, 31, 11);
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.delivery(deliveryStartDate)
        );
    }

    @Test
    @DisplayName("4-1. 상품 배송완료 - 상품의 배송이 완료됨을 처리한다.")
    void delivery_end_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY);

        // when
        LocalDateTime deliveryCompleteDate =
                LocalDateTime.of(2023, 5, 20, 15, 31, 11);
        String deliveryPlace = "현관문 앞";
        orderItem.deliveryEnd(deliveryCompleteDate, deliveryPlace);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_END);
        assertThat(orderItem.getDeliveryCompleteDate()).isEqualTo(deliveryCompleteDate);
        assertThat(orderItem.getDeliveryPlace()).isEqualTo(deliveryPlace);
    }

    @Test
    @DisplayName("4-2. 배송완료 시점은 배송 시작 시점보다 나중이어야 한다.")
    void delivery_end_fail_with_after_delivery_start_date_more_complete_date() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.OUTING);
        LocalDateTime deliveryStartDate =
                LocalDateTime.of(2023, 5, 20, 15, 31, 11);
        orderItem.delivery(deliveryStartDate);

        // when & then
        LocalDateTime deliveryCompleteDate =
                LocalDateTime.of(2023, 5, 10, 15, 31, 11);
        String deliveryPlace = "현관문 앞";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.deliveryEnd(deliveryCompleteDate, deliveryPlace)
        );
    }

    @Test
    @DisplayName("5-1. 구매확정 - 배송완료한 상품의 구매를 확정한다.")
    void finish_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when
        orderItem.finish();

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.FINISH);
    }

    @Test
    @DisplayName("5-2. 배송이 완료된 상품이 아니면 구매확정이 불가하다.")
    void finish_fail_with_not_delivery_end() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                orderItem::finish
        );
    }

    @Test
    @DisplayName("5-3. 구매확정 처리가 되면 해당 상품의 판매건수가 증가한다.")
    void finish_and_product_sales_volume_increase() {
        // given
        int orderQuantity = 2;
        OrderItem orderItem = new OrderItem(product, orderQuantity, OrderStatus.DELIVERY_END);

        // when
        orderItem.finish();

        // then
        assertThat(product.getSalesVolume()).isEqualTo(orderQuantity);
    }

    @Test
    @DisplayName("6-1. 배송완료된 상품을 교환신청한다.")
    void exchange_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when
        LocalDateTime exchangeDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String exchangeReason = "교환 신청 합니다.";
        orderItem.exchange(exchangeDate, exchangeReason);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.EXCHANGE);
    }

    @Test
    @DisplayName("6-2. 배송완료 상태가 아닌 상품은 교환신청이 불가하다.")
    void exchange_fail_with_not_delivery_end() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.FINISH);

        // when & then
        LocalDateTime exchangeDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String exchangeReason = "교환 신청 합니다.";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.exchange(exchangeDate, exchangeReason)
        );
    }

    @Test
    @DisplayName("6-3. 교환사유 없이는 교환신청이 불가하다.")
    void exchange_fail_with_not_content_reason() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when & then
        LocalDateTime exchangeDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String exchangeReason = "";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.exchange(exchangeDate, exchangeReason)
        );
    }

    @Test
    @DisplayName("7-1. 배송완료된 상품을 환불신청한다.")
    void refund_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when
        LocalDateTime refundDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String refundReason = "교환 신청 합니다.";
        orderItem.refund(refundDate, refundReason);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.REFUND);
    }

    @Test
    @DisplayName("7-2. 배송완료 상태가 아닌 상품은 교환신청이 불가하다.")
    void refund_fail_with_not_delivery_end() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.FINISH);

        // when & then
        LocalDateTime refundDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String refundReason = "교환 신청 합니다.";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.refund(refundDate, refundReason)
        );
    }

    @Test
    @DisplayName("7-3. 교환사유 없이는 교환신청이 불가하다.")
    void refund_fail_with_not_content_reason() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when & then
        LocalDateTime refundDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String refundReason = "";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.refund(refundDate, refundReason)
        );
    }

    @Test
    @DisplayName("8-1. 검수중 - 교환 요청된 상품을 검수한다.")
    void checking_test_with_exchange() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.EXCHANGE);

        // when
        orderItem.checking();

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.CHECKING);
    }

    @Test
    @DisplayName("8-2. 검수중 - 환불 요청된 상품을 검수한다.")
    void checking_test_with_refund() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.REFUND);

        // when
        orderItem.checking();

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.CHECKING);
    }

    @Test
    @DisplayName("8-3. 교환/환불 대상이 아닌 상품은 검수중 상태로 처리할 수 없다.")
    void checking_fail_test_with_not_refund_and_exchange() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.DELIVERY_END);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                orderItem::checking
        );
    }

    @Test
    @DisplayName("9-1. 환불완료 - 상품 결제 금액 만큼 환불을 완료한다.")
    void refund_end_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.REFUND);

        // when
        orderItem.refundEnd();

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.REFUND_END);
    }

    @Test
    @DisplayName("10-1. 주문 취소 시, 취소시간/사유를 기록한다.")
    void cancel_test() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.READY);

        // when
        LocalDateTime cancelDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String cancelReason = "취소 사유 입니다.";
        orderItem.cancel(cancelDate, cancelReason);

        // then
        assertThat(orderItem.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(product.getCount()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("10-2. 취소사유를 입력하지 않으면 주문취소가 불가하다.")
    void cancel_fail_with_not_ready() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.OUTING);

        // when & then
        LocalDateTime cancelDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String cancelReason = "취소 사유 입니다.";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.cancel(cancelDate, cancelReason)
        );
    }

    @Test
    @DisplayName("10-3. 취소사유를 입력하지 않으면 주문취소가 불가하다.")
    void cancel_fail_with_no_content_reason() {
        // given
        OrderItem orderItem = new OrderItem(product, 2, OrderStatus.READY);

        // when & then
        LocalDateTime cancelDate = LocalDateTime.of(2023, 5, 8, 12, 0, 0);
        String cancelReason = "";
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderItem.cancel(cancelDate, cancelReason)
        );
    }
}