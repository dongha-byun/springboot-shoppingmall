package springboot.shoppingmall.user.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "delivery")
@Entity
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String receiverName;

    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Delivery(String nickName, String receiverName, String zipCode, String address, String detailAddress,
                    String requestMessage, User user) {
        this.nickName = nickName;
        this.receiverName = receiverName;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.user = user;
    }

    public Delivery createBy(User user){
        this.user = user;
        user.addDelivery(this);
        return this;
    }
}
