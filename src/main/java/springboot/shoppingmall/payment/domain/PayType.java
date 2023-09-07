package springboot.shoppingmall.payment.domain;

import lombok.Getter;

@Getter
public enum PayType {
    CARD("신용/체크카드"),
    KAKAO_PAY("카카오페이");

    private final String typeName;

    PayType(String typeName) {
        this.typeName = typeName;
    }
}
