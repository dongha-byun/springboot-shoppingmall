package springboot.shoppingmall.user.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import springboot.shoppingmall.userservice.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "payment")
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayType type;

    @Enumerated(EnumType.STRING)
    private CardCompany cardCom;

    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;

    private String expireMM;
    private String expireYY;

    private String cvc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Payment(Long id, PayType type, CardCompany cardCom, String cardNo1, String cardNo2, String cardNo3, String cardNo4,
                   String expireMM, String expireYY, String cvc) {
        this.id = id;
        this.type = type;
        this.cardCom = cardCom;
        this.cardNo1 = cardNo1;
        this.cardNo2 = cardNo2;
        this.cardNo3 = cardNo3;
        this.cardNo4 = cardNo4;
        this.expireMM = expireMM;
        this.expireYY = expireYY;
        this.cvc = cvc;
    }

    @Builder
    public Payment(PayType type, CardCompany cardCom, String cardNo1, String cardNo2, String cardNo3, String cardNo4,
                   String expireMM, String expireYY, String cvc) {
        this.type = type;
        this.cardCom = cardCom;
        this.cardNo1 = cardNo1;
        this.cardNo2 = cardNo2;
        this.cardNo3 = cardNo3;
        this.cardNo4 = cardNo4;
        this.expireMM = expireMM;
        this.expireYY = expireYY;
        this.cvc = cvc;
    }

    public Payment byUser(User user) {
        this.user = user;
        if(user != null){
            user.addPayment(this);
        }

        return this;
    }
}
