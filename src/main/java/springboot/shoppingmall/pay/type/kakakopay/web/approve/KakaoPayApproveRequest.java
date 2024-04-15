package springboot.shoppingmall.pay.type.kakakopay.web.approve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveRequestDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayApproveRequest {
    private String tid;
    private String partner_order_id;
    private String pg_token;

    public KakaoPayApproveRequestDto toDto(String userId) {
        return new KakaoPayApproveRequestDto(
                this.tid,
                this.partner_order_id,
                userId,
                this.pg_token
        );
    }
}
