package springboot.shoppingmall.authorization.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailAuthorizationProcessor {
    private final EmailSender emailSender;
    @Value("${authorization.mail.title}")
    private String title;

    public void sendAuthorizationMail(String email, String code) {
        emailSender.send(email, title, makeContent(code));
    }

    private String makeContent(String code) {
        return code;
    }
}
