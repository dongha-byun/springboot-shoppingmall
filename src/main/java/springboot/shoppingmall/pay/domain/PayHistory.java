package springboot.shoppingmall.pay.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.payment.domain.PayType;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PayHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    private PayType payType;

    @Column(nullable = false)
    private String tid;

    @Column(nullable = false)
    private Integer amount;

    public PayHistory(Long orderId, PayType payType, String tid, Integer amount) {
        this.orderId = orderId;
        this.payType = payType;
        this.tid = tid;
        this.amount = amount;
    }

    public static PayHistory create(Long orderId, PayType payType, String tid, Integer amount) {
        return new PayHistory(orderId, payType, tid, amount);
    }
}
