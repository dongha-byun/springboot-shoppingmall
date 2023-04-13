package springboot.shoppingmall.pay.web;

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

    public String toDataUrlEncoded() {
        StringBuilder sb = new StringBuilder();
        sb.append("cid=").append(cid);
        sb.append("&partner_order_id=").append(partner_order_id);
        sb.append("&partner_user_id=").append(partner_user_id);
        sb.append("&item_name=").append(item_name);
        sb.append("&quantity=").append(quantity);
        sb.append("&total_amount=").append(total_amount);
        sb.append("&vat_amount=").append(vat_amount);
        sb.append("&tax_free_amount=").append(tax_free_amount);
        sb.append("&approval_url=").append(approval_url);
        sb.append("&fail_url=").append(fail_url);
        sb.append("&cancel_url=").append(cancel_url);

        return sb.toString();
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("cid", this.cid);
        map.put("partner_order_id", this.partner_order_id);
        map.put("partner_user_id", this.partner_user_id);
        map.put("item_name", this.item_name);
        map.put("quantity", String.valueOf(this.quantity));
        map.put("total_amount", String.valueOf(this.total_amount));
        map.put("vat_amount", String.valueOf(this.vat_amount));
        map.put("tax_free_amount", String.valueOf(this.tax_free_amount));
        map.put("approval_url", this.approval_url);
        map.put("fail_url", this.fail_url);
        map.put("cancel_url", this.cancel_url);

        return map;
    }

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
