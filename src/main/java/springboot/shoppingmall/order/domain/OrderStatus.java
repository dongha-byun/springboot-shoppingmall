package springboot.shoppingmall.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    PAY_WAIT("결제 대기중"),
    PREPARED("상품 준비중"),
    CANCEL("주문 취소"),
    OUTING("상품 출고"),
    DELIVERY("배송중"),
    DELIVERY_END("배송완료"),
    REFUND("환불 요청"),
    REFUND_END("환불 완료"),
    EXCHANGE("교환 요청"),
    EXCHANGE_END("교환 완료"),
    CHECKING("상품 검수중"),
    FINISH("구매 확정");

    private final String statusName;
}
