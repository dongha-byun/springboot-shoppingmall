package springboot.shoppingmall.authorization.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_EXISTS_REFRESH_TOKEN(-501, "인증과정에서 오류가 발생했습니다. 다시 로그인 해주세요."),
    OVER_QUANTITY(-502, "주문 수량이 상품 재고 수량보다 많아 주문이 불가능합니다.");

    private final int code;
    private final String message;
}
