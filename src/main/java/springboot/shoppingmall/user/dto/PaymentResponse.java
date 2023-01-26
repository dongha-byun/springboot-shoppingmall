package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.Payment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String cardNo1;
    private String cardNo2;
    private String cardNo3;
    private String cardNo4;
    private String cardCompanyName;
    private String payTypeName;

    public static PaymentResponse of(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getCardNo1(), payment.getCardNo2(), payment.getCardNo3(),
                payment.getCardNo4(), payment.getCardCom().getCompanyName(), payment.getType().getTypeName());
    }
}
