package springboot.shoppingmall.providers.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProviderLoginRequest {
    private String loginId;
    private String password;
}
