package springboot.shoppingmall.authorization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;
import springboot.shoppingmall.authorization.service.EmailAuthorizationService;

@RequiredArgsConstructor
@RestController
public class EmailAuthorizationController {
    private final EmailAuthorizationService service;

    @PostMapping("/send-authorize-code")
    public ResponseEntity<String> sendAuthorizationCode(@RequestBody AuthorizationMailRequest mailRequest) {
        EmailAuthorizationCode emailAuthorizationCode = service.createCode(mailRequest.toValue());
        return ResponseEntity.ok().body(emailAuthorizationCode.getValue());
    }

    @PostMapping("/check-authorized-code")
    public ResponseEntity<String> checkAuthorizedCode(@RequestBody AuthorizationRequest authorizationRequest) {
        service.checkCode(authorizationRequest.getEmailValue(), authorizationRequest.getCodeValue());
        return ResponseEntity.ok().body("ok");
    }
}
