package springboot.shoppingmall.delivery.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private String receiverPhoneNumber;

    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private Long userId;

    @Builder
    public Delivery(String nickName, String receiverName, String receiverPhoneNumber,
                    String zipCode, String address, String detailAddress,
                    String requestMessage, Long userId) {
        this.nickName = nickName;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.userId = userId;
    }

    public Delivery createBy(Long userId){
        this.userId = userId;
        return this;
    }
}
