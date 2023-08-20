package springboot.shoppingmall.providers.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProviderLoginRequest {
    private String loginId;
    private String password;
}
