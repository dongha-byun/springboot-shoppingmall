package springboot.shoppingmall.pay.type.card.web;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CardApproveResponse {
    private String cid;
    private String tid;
    private LocalDateTime approved_at;
}
