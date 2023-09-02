package springboot.shoppingmall.userservice.authentication.email.presentation;

import java.net.URI;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.userservice.authentication.email.application.EmailAuthorizationService;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthorizationInfo;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthorizationMailRequest;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthorizationRequest;
import springboot.shoppingmall.userservice.authentication.email.presentation.response.EmailAuthorizationSuccessResponse;

@RequiredArgsConstructor
@RestController
public class EmailAuthorizationController {
    private final EmailAuthorizationService service;

    @PostMapping("/send-authorize-code")
    public ResponseEntity<EmailAuthorizationSuccessResponse> sendAuthorizationCode(@RequestBody AuthorizationMailRequest mailRequest) {
        LocalDateTime now = LocalDateTime.now();
        EmailAuthorizationInfo authorizationInfo = service.createCode(mailRequest.toValue(), now);
        return ResponseEntity.created(URI.create("http://localhost:3000/authorized-code-form")).body(
                EmailAuthorizationSuccessResponse.of(authorizationInfo));
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
