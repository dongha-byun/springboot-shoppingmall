package springboot.shoppingmall.partners.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerTokenResponse {
    private String accessToken;
    private String name;
}
