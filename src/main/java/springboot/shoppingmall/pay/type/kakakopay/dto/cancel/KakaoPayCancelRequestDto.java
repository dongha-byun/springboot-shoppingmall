package springboot.shoppingmall.pay.type.kakakopay.dto.cancel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayCancelRequestDto {
    private String tid;
    private int cancel_amount;
    private int cancel_tax_free_amount;


    public MultiValueMap<String, Object> toFormData() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("tid", this.tid);
        map.add("cancel_amount", this.cancel_amount);
        map.add("cancel_tax_free_amount", this.cancel_tax_free_amount);

        return map;
    }

}
