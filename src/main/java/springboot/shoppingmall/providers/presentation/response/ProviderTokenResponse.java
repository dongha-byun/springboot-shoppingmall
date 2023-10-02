package springboot.shoppingmall.providers.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderTokenResponse {
    private String accessToken;
    private String name;
}
