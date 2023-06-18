package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
@Entity
public class Coupon extends BaseEntity {

    @EmbeddedId
    private CouponCode couponCode;

    @Embedded
    private UsingDuration usingDuration;

    private Long userId;

    private int discountRate;

    private Long partnersId;

    private boolean isUsed;

    public Coupon(CouponCode couponCode, UsingDuration usingDuration, Long userId, int discountRate, Long partnersId) {
        this(couponCode, usingDuration, userId, discountRate, partnersId, false);
    }

    public Coupon(CouponCode couponCode, UsingDuration usingDuration, Long userId, int discountRate, Long partnersId,
                  boolean isUsed) {
        this.couponCode = couponCode;
        this.usingDuration = usingDuration;
        this.userId = userId;
        this.discountRate = discountRate;
        this.partnersId = partnersId;
        this.isUsed = isUsed;
    }

    public static Coupon create(LocalDateTime fromDate, LocalDateTime toDate,
                                Long userId, int discountRate, Long partnersId) {
        String code = UUID.randomUUID().toString();
        return new Coupon(
                new CouponCode(code), new UsingDuration(fromDate, toDate), userId, discountRate, partnersId
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return Objects.equals(getCouponCode(), coupon.getCouponCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCouponCode());
    }
}
