package springboot.shoppingmall.payment.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.payment.application.dto.PaymentCreateDto;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.domain.Payment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;

    private String expireMM;
    private String expireYY;
    private String cvc;

    private String payType;
    private String cardCom;

    public PaymentCreateDto toDto() {
        return PaymentCreateDto.builder()
                .cardNo1(cardNo1).cardNo2(cardNo2).cardNo3(cardNo3).cardNo4(cardNo4)
                .expireMM(expireMM).expireYY(expireYY).cvc(cvc)
                .payType(PayType.valueOf(payType))
                .cardCom(CardCompany.valueOf(cardCom))
                .build();
    }
}
