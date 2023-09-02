package springboot.shoppingmall.userservice.authentication.email.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailAuthorizationProcessor {
    private final MailNotifier mailNotifier;

    @Value("${authorization.mail.signup.title}")
    private String title;

    public void sendAuthorizationMail(String email, String code) {
        mailNotifier.send(email, title, makeContent(code));
    }

    private String makeContent(String code) {
        return code;
    }
}
