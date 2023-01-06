package springboot.shoppingmall.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    READY("상품 준비중"),
    CANCEL("주문 취소"),
    OUTING("상품 출고"),
    DELIVERY("배송중"),
    END("배송완료"),
    RETURN_REQ("환불 요청"),
    RETURN_END("환불 완료"),
    EXCHANGE_REQ("교환 요청"),
    CHECKING("상품 검수중"),
    FINISH("구매 확정");

    private final String statusName;
}
