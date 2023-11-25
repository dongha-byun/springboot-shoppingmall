package springboot.shoppingmall.partners.presentation;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.partners.application.PartnerLoginService;
import springboot.shoppingmall.partners.presentation.request.PartnerLoginRequest;
import springboot.shoppingmall.partners.presentation.response.PartnerTokenResponse;

@RequiredArgsConstructor
@BeanValidation
@RestController
public class PartnersLoginController {
    private final PartnerLoginService loginService;

    @PostMapping("/partners/login")
    public ResponseEntity<PartnerTokenResponse> login(@RequestBody @Valid PartnerLoginRequest loginRequest,
                                                      BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new BeanValidationException(bindingResult);
        }
        String accessToken = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        return ResponseEntity.ok().body(new PartnerTokenResponse(accessToken));
    }
}
