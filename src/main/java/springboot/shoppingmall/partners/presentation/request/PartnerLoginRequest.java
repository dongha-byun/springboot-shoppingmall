package springboot.shoppingmall.partners.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnerLoginRequest {
    private String loginId;
    private String password;
}
