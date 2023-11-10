package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private UsingDuration usingDuration;

    private int discountRate;

    private Long partnerId;

    @OneToMany(mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<UserCoupon> userCoupons = new ArrayList<>();

    public Coupon(String name, UsingDuration usingDuration, int discountRate, Long partnerId) {
        this.name = name;
        this.usingDuration = usingDuration;
        this.discountRate = discountRate;
        this.partnerId = partnerId;
    }

    public static Coupon create(String name, LocalDateTime fromDate, LocalDateTime toDate, int discountRate, Long partnersId) {
        return new Coupon(name, new UsingDuration(fromDate, toDate), discountRate, partnersId);
    }

    public void addUserCoupon(Long userId) {
        this.userCoupons.add(
                UserCoupon.create(userId, this)
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
        return Objects.equals(getId(), coupon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
