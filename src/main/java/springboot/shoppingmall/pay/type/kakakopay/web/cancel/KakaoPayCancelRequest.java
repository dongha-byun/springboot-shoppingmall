package springboot.shoppingmall.pay.type.kakakopay.web.cancel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelRequestDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoPayCancelRequest {
    private String tid;
    private int cancel_amount;
    private int cancel_tax_free_amount;

    public KakaoPayCancelRequestDto toDto() {
        return new KakaoPayCancelRequestDto(
                this.tid,
                this.cancel_amount,
                this.cancel_tax_free_amount
        );
    }
}
