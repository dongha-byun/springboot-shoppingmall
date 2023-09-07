package springboot.shoppingmall.payment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.domain.Payment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentDto {
    private Long id;
    private PayType payType;
    private CardCompany cardCompany;
    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;
    private String expireMM;
    private String expireYY;
    private String cvc;

    public static PaymentDto of(Payment entity) {
        return new PaymentDto(
                entity.getId(), entity.getType(), entity.getCardCom(),
                entity.getCardNo1(), entity.getCardNo2(), entity.getCardNo3(), entity.getCardNo4(),
                entity.getExpireMM(), entity.getExpireYY(), entity.getCvc()
        );
    }
}
