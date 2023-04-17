package springboot.shoppingmall.providers.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.service.ProviderLoginService;

@RequiredArgsConstructor
@RestController
public class ProviderLoginController {
    private final ProviderLoginService loginService;

    @GetMapping("/providers/login")
    public ResponseEntity<ProviderResponse> login(@RequestBody ProviderLoginRequest loginRequest) {
        ProviderDto dto = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        ProviderResponse response = ProviderResponse.of(dto);
        return ResponseEntity.ok().body(response);
    }
}
