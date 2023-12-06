package springboot.shoppingmall.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderItemResolutionType {
    CANCEL("주문이 성공적으로 취소되었습니다."),
    REFUND("상품 환불 절차를 진행합니다."),
    EXCHANGE("상품 교환 절차를 진행합니다.");

    private final String completeMessage;
}
