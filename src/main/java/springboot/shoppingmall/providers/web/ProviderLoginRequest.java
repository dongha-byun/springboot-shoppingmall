package springboot.shoppingmall.providers.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderLoginRequest {
    private String loginId;
    private String password;
}
