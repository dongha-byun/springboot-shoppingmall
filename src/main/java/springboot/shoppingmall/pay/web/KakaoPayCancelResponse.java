package springboot.shoppingmall.pay.web;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KakaoPayCancelResponse {
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class ApprovedCancelAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class CanceledAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class CancelAvailableAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }
}
