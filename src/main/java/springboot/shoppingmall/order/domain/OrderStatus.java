package springboot.shoppingmall.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    READY("상품 준비중"),
    END("배송완료"),
    RETURN_REQ("환불 요청"),
    RETURN_END("환불 완료"),
    EXCHANGE_REQ("교환 요청"),
    EXCHANGE_END("교환 완료"),
    CHECKING("상품 검수중");

    private final String statusName;
}
