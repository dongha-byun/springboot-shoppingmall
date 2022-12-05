package springboot.shoppingmall.user.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import springboot.shoppingmall.BaseEntity;

@Entity
@Getter
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayType type;

    private String cardCom;

    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;
}
