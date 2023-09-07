package springboot.shoppingmall.payment.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

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

    public static PaymentResponse of(PaymentDto dto) {
        return new PaymentResponse(dto.getId(), dto.getCardNo1(), dto.getCardNo2(), dto.getCardNo3(),
                dto.getCardNo4(), dto.getCardCompany().getCompanyName(), dto.getPayType().getTypeName());
    }
}
