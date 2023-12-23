package springboot.shoppingmall.pay.type.kakakopay.web.ready;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyRequestDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayReadyRequest {
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

    public KakaoPayReadyRequestDto toDto() {
        return new KakaoPayReadyRequestDto(
                this.partner_order_id, this.partner_user_id,
                this.item_name, this.quantity,
                this.total_amount, this.vat_amount, this.tax_free_amount,
                this.approval_url, this.fail_url, this.cancel_url
        );
    }
}
