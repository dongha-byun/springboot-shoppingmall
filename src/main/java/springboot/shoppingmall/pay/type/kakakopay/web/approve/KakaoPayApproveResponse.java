package springboot.shoppingmall.pay.type.kakakopay.web.approve;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.value.Amount;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayApproveResponse {
    private String cid;
    private String aid;
    private String tid;
    private String partner_user_id;
    private String partner_order_id;
    private String payment_method_type;
    private String item_name;
    private Integer quantity;
    private Amount amount;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;

    public static KakaoPayApproveResponse of(KakaoPayApproveResponseDto dto) {
        return new KakaoPayApproveResponse(
                dto.getCid(), dto.getAid(), dto.getTid(),
                dto.getPartner_user_id(), dto.getPartner_order_id(),
                dto.getPayment_method_type(), dto.getItem_name(),
                dto.getQuantity(), dto.getAmount(),
                dto.getCreated_at(), dto.getApproved_at()
        );
    }
}
