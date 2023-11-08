package springboot.shoppingmall.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {
    OVER_QUANTITY(-502, "주문 수량이 상품 재고 수량보다 많아 주문이 불가능합니다.");

    private final int code;
    private final String message;
}
