package springboot.shoppingmall.pay.type.kakakopay.dto.cancel;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.pay.type.kakakopay.value.Amount;
import springboot.shoppingmall.pay.type.kakakopay.value.ApprovedCancelAmount;
import springboot.shoppingmall.pay.type.kakakopay.value.CancelAvailableAmount;
import springboot.shoppingmall.pay.type.kakakopay.value.CanceledAmount;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoPayCancelResponseDto {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private ApprovedCancelAmount approvedCancelAmount;
    private CanceledAmount canceledAmount;
    private CancelAvailableAmount cancelAvailableAmount;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;
}
