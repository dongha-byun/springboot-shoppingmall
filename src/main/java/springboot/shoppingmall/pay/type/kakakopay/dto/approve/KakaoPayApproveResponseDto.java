package springboot.shoppingmall.pay.type.kakakopay.dto.approve;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.pay.type.kakakopay.value.Amount;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayApproveResponseDto {
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
}
