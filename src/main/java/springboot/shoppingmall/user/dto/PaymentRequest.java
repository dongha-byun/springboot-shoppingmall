package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.CardCompany;
import springboot.shoppingmall.user.domain.PayType;
import springboot.shoppingmall.user.domain.Payment;

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

    public static Payment to(PaymentRequest paymentRequest) {
        return new Payment(PayType.valueOf(paymentRequest.getPayType()),
                CardCompany.valueOf(paymentRequest.getCardCom()),
                paymentRequest.getCardNo1(), paymentRequest.getCardNo2(), paymentRequest.getCardNo3(),
                paymentRequest.getCardNo4(), paymentRequest.getExpireMM(), paymentRequest.getExpireYY(),
                paymentRequest.getCvc());
    }
}
