package springboot.shoppingmall.pay.type.card.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CardApproveRequest {
    private String cid;
    private String tid;

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("cid", this.cid);
        map.add("tid", this.tid);

        return map;
    }
}
