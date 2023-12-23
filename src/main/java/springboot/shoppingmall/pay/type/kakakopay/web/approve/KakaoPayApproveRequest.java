package springboot.shoppingmall.pay.type.kakakopay.web.approve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveRequestDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayApproveRequest {
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

    public KakaoPayApproveRequestDto toDto() {
        return new KakaoPayApproveRequestDto(
                this.tid,
                this.partner_order_id,
                this.partner_user_id,
                this.pg_token
        );
    }
}
