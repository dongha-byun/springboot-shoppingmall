package springboot.shoppingmall.pay.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KakaoPayCancelRequest {
    private String cid;
    private String tid;
    private int cancel_amount;
    private int cancel_tax_free_amount;

    public MultiValueMap<String, Object> toFormData() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("cid", this.cid);
        map.add("tid", this.tid);
        map.add("cancel_amount", this.cancel_amount);
        map.add("cancel_tax_free_amount", this.cancel_tax_free_amount);

        return map;
    }
}
