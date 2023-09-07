package springboot.shoppingmall.payment.domain;

import lombok.Getter;

@Getter
public enum CardCompany {
    SH("신한카드"),
    KB("국민카드"),
    HD("현대카드"),
    SS("삼성카드");

    private String companyName;

    CardCompany(String companyName) {
        this.companyName = companyName;
    }
}
