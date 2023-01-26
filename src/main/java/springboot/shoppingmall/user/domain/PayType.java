package springboot.shoppingmall.user.domain;

import lombok.Getter;

@Getter
public enum PayType {
    CREDIT_CARD("신용카드"),
    CHECK_CARD("체크카드");

    private final String typeName;

    PayType(String typeName) {
        this.typeName = typeName;
    }
}
