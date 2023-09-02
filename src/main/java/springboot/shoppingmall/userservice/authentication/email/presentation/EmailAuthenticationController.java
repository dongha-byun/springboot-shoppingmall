package springboot.shoppingmall.userservice.authentication.email.presentation;

import java.net.URI;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.userservice.authentication.email.application.EmailAuthenticationService;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthenticationMailRequest;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthenticationRequest;
import springboot.shoppingmall.userservice.authentication.email.presentation.response.EmailAuthenticationSuccessResponse;

@RequiredArgsConstructor
@RestController
public class EmailAuthenticationController {
    private final EmailAuthenticationService service;

    @PostMapping("/send-authorize-code")
    public ResponseEntity<EmailAuthenticationSuccessResponse> sendAuthorizationCode(@RequestBody AuthenticationMailRequest mailRequest) {
        LocalDateTime now = LocalDateTime.now();
        EmailAuthenticationInfo authorizationInfo = service.createCode(mailRequest.toValue(), now);
        return ResponseEntity.created(URI.create("http://localhost:3000/authorized-code-form")).body(
                EmailAuthenticationSuccessResponse.of(authorizationInfo));
    }

    @PostMapping("/check-authorized-code")
    public ResponseEntity<EmailAuthenticationSuccessResponse> checkAuthorizedCode(@RequestBody AuthenticationRequest authorizationRequest) {
        LocalDateTime now = LocalDateTime.now();
        EmailAuthenticationInfo info = service.checkCode(
                authorizationRequest.getEmailValue(), authorizationRequest.getCodeValue(), now
        );
        return ResponseEntity.ok().body(EmailAuthenticationSuccessResponse.of(info));
    }
}
