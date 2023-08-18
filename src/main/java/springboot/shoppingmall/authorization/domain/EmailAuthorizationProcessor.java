package springboot.shoppingmall.authorization.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailAuthorizationProcessor {
    private final EmailSender emailSender;
    private static final String TITLE = "[쇼핑몰 프로젝트] 회원가입 인증번호 발송";

    public void sendAuthorizationMail(String email, String code) {
        emailSender.send(email, TITLE, makeContent(code));
    }

    private String makeContent(String code) {
        return code;
    }
}
