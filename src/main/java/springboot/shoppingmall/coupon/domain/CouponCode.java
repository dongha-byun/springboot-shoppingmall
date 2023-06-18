package springboot.shoppingmall.coupon.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CouponCode implements Serializable {

    @Column(name = "coupon_code")
    private String code;

    public CouponCode(String code) {
        this.code = code;
    }
}
