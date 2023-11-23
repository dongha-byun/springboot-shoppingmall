package springboot.shoppingmall.partners.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnerTokenResponse {
    private String accessToken;
}
