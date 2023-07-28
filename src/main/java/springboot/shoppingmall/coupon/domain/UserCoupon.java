package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime usingDate;

    public UserCoupon(Long userId, Coupon coupon, LocalDateTime usingDate) {
        this(null, userId, coupon, usingDate);
    }

    public UserCoupon(Long id, Long userId, Coupon coupon, LocalDateTime usingDate) {
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
        this.usingDate = usingDate;
    }

    public static UserCoupon create(Long userId, Coupon coupon) {
        return new UserCoupon(userId, coupon, null);
    }

    public void use() {
        this.usingDate = LocalDateTime.now();
    }
}
