package springboot.shoppingmall.pay.type.kakakopay.web;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }
}
