package springboot.shoppingmall.pay.type.kakakopay.dto.ready;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayReadyRequestDto {
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

    public MultiValueMap<String, Object> toFormData() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("partner_order_id", this.partner_order_id);
        map.add("partner_user_id", this.partner_user_id);
        map.add("item_name", this.item_name);
        map.add("quantity", this.quantity);
        map.add("total_amount", this.total_amount);
        map.add("vat_amount", this.vat_amount);
        map.add("tax_free_amount", this.tax_free_amount);
        map.add("approval_url", this.approval_url);
        map.add("fail_url", this.fail_url);
        map.add("cancel_url", this.cancel_url);

        return map;
    }
}
