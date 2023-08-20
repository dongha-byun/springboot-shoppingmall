package springboot.shoppingmall.authorization.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.authorization.domain.Email;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationProcessor;

@Transactional
@RequiredArgsConstructor
@Service
public class EmailAuthorizationService {
    private final EmailAuthorizationCodeStore store;
    private final EmailAuthorizationProcessor emailAuthorizationProcessor;
    private final AuthorizationCodeGenerator codeGenerator;

    public EmailAuthorizationCode createCode(Email email) {
        // 1. create code
        String code = codeGenerator.generate();

        // 2. save code
        EmailAuthorizationCode authCode = new EmailAuthorizationCode(code);
        store.save(email, authCode);

        // 3. send email
        // 비동기 처리 필요
        emailAuthorizationProcessor.sendAuthorizationMail(email.getValue(), authCode.getValue());

        return authCode;
    }

    public void checkCode(Email email, EmailAuthorizationCode code) {
        EmailAuthorizationCode findCode = store.getCode(email);
        if(!findCode.equals(code)) {
            throw new IllegalArgumentException("인증번호가 맞지 않습니다.");
        }
        store.remove(email);
    }
}
