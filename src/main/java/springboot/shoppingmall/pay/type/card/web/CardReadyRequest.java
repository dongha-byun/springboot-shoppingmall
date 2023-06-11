package springboot.shoppingmall.pay.type.card.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CardReadyRequest {
    private String cid;
    private String item_name;
    private Integer quantity;
    private Integer total_amount;
    private String approval_url;
    private String fail_url;

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("cid", this.cid);
        map.add("item_name", this.item_name);
        map.add("quantity", String.valueOf(this.quantity));
        map.add("total_amount", String.valueOf(this.total_amount));
        map.add("approval_url", this.approval_url);
        map.add("fail_url", this.fail_url);

        return map;
    }
}
