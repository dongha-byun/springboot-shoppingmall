package springboot.shoppingmall.pay.type.kakakopay.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoPayApproveRequest {
    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("cid", this.cid);
        map.add("tid", this.tid);
        map.add("partner_order_id", this.partner_order_id);
        map.add("partner_user_id", this.partner_user_id);
        map.add("pg_token", this.pg_token);

        return map;
    }
}
