package springboot.shoppingmall.providers.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.application.ProviderLoginService;
import springboot.shoppingmall.providers.presentation.request.ProviderLoginRequest;
import springboot.shoppingmall.providers.presentation.response.ProviderTokenResponse;

@RequiredArgsConstructor
@RestController
public class PartnersLoginController {
    private final ProviderLoginService loginService;

    @PostMapping("/partners/login")
    public ResponseEntity<ProviderTokenResponse> login(@RequestBody ProviderLoginRequest loginRequest) {
        ProviderDto dto = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        String accessToken = "access-token";

        return ResponseEntity.ok().body(new ProviderTokenResponse(accessToken, dto.getName()));
    }
}
