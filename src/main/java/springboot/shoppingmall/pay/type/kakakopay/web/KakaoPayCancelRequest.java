package springboot.shoppingmall.pay.type.kakakopay.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KakaoPayCancelRequest {
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
