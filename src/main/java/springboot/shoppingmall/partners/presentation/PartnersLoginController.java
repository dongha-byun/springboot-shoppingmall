package springboot.shoppingmall.partners.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.partners.dto.PartnerDto;
import springboot.shoppingmall.partners.application.PartnerLoginService;
import springboot.shoppingmall.partners.presentation.request.PartnerLoginRequest;
import springboot.shoppingmall.partners.presentation.response.PartnerTokenResponse;

@RequiredArgsConstructor
@RestController
public class PartnersLoginController {
    private final PartnerLoginService loginService;

    @PostMapping("/partners/login")
    public ResponseEntity<PartnerTokenResponse> login(@RequestBody PartnerLoginRequest loginRequest) {
        PartnerDto dto = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        String accessToken = "access-token";

        return ResponseEntity.ok().body(new PartnerTokenResponse(accessToken, dto.getName()));
    }
}
