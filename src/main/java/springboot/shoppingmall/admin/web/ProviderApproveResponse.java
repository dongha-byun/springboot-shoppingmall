package springboot.shoppingmall.admin.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderApproveResponse {
    private Long id;
    private boolean isApproved;
}
