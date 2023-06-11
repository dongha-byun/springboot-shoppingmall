package springboot.shoppingmall.pay.type.kakakopay.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.scaffold.MethodGraph.Linked;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoPayReadyRequest implements Serializable {
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private Integer quantity;
    private Integer total_amount;
    private Integer vat_amount;
    private Integer tax_free_amount;
    private String approval_url;
    private String fail_url;
    private String cancel_url;

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("cid", this.cid);
        map.add("partner_order_id", this.partner_order_id);
        map.add("partner_user_id", this.partner_user_id);
        map.add("item_name", this.item_name);
        map.add("quantity", String.valueOf(this.quantity));
        map.add("total_amount", String.valueOf(this.total_amount));
        map.add("vat_amount", String.valueOf(this.vat_amount));
        map.add("tax_free_amount", String.valueOf(this.tax_free_amount));
        map.add("approval_url", this.approval_url);
        map.add("fail_url", this.fail_url);
        map.add("cancel_url", this.cancel_url);

        return map;
    }
}
