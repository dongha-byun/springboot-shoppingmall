package springboot.shoppingmall.providers.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.service.ProviderLoginService;

@RequiredArgsConstructor
@RestController
public class PartnersLoginController {
    private final ProviderLoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/partners/login")
    public ResponseEntity<ProviderTokenResponse> login(@RequestBody ProviderLoginRequest loginRequest) {
        ProviderDto dto = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        String accessToken = jwtTokenProvider.createRefreshToken(dto.getId(), "127.0.0.1");

        return ResponseEntity.ok().body(new ProviderTokenResponse(accessToken));
    }
}
