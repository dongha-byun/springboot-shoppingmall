package springboot.shoppingmall.authorization.controller;

import java.net.URI;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.service.EmailAuthorizationInfo;
import springboot.shoppingmall.authorization.service.EmailAuthorizationService;

@RequiredArgsConstructor
@RestController
public class EmailAuthorizationController {
    private final EmailAuthorizationService service;

    @PostMapping("/send-authorize-code")
    public ResponseEntity<EmailAuthorizationResponse> sendAuthorizationCode(@RequestBody AuthorizationMailRequest mailRequest) {
        LocalDateTime now = LocalDateTime.now();
        EmailAuthorizationInfo authorizationInfo = service.createCode(mailRequest.toValue(), now);
        return ResponseEntity.created(URI.create("http://localhost:3000/authorized-code-form")).body(EmailAuthorizationResponse.of(authorizationInfo));
    }

    @PostMapping("/check-authorized-code")
    public ResponseEntity<EmailAuthorizationSuccessResponse> checkAuthorizedCode(@RequestBody AuthorizationRequest authorizationRequest) {
        LocalDateTime now = LocalDateTime.now();
        EmailAuthorizationInfo info = service.checkCode(
                authorizationRequest.getEmailValue(), authorizationRequest.getCodeValue(), now
        );
        return ResponseEntity.ok().body(EmailAuthorizationSuccessResponse.of(info));
    }
}
