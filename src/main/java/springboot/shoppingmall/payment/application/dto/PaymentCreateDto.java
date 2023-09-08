package springboot.shoppingmall.payment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.domain.Payment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentCreateDto {
    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;

    private String expireMM;
    private String expireYY;
    private String cvc;

    private PayType payType;
    private CardCompany cardCom;
    public Payment toEntity() {
        return Payment.builder()
                .cardNo1(cardNo1).cardNo2(cardNo2).cardNo3(cardNo3).cardNo4(cardNo4)
                .expireMM(expireMM).expireYY(expireYY).cvc(cvc)
                .payType(payType).cardCom(cardCom)
                .build();
    }
}
