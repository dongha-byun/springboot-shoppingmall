package springboot.shoppingmall.authorization.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

    public EmailAuthorizationInfo createCode(Email email, LocalDateTime requestTime) {
        // 1. create code
        String code = codeGenerator.generate();

        // 2. save code
        EmailAuthorizationCode authCode = new EmailAuthorizationCode(code, requestTime);
        store.save(email, authCode);

        // 3. send email
        // 비동기 처리 필요
        emailAuthorizationProcessor.sendAuthorizationMail(email.getValue(), authCode.getValue());

        return EmailAuthorizationInfo.of(email, authCode);
    }

    public EmailAuthorizationInfo checkCode(Email email, EmailAuthorizationCode code, LocalDateTime checkRequestTime) {
        EmailAuthorizationCode findCode = store.getCode(email);
        if(checkRequestTime.isAfter(findCode.getExpireTime())) {
            return createCode(email, checkRequestTime);
        }

        if(!findCode.equals(code)) {
            throw new IllegalArgumentException("인증번호가 맞지 않습니다.");
        }
        store.remove(email);
        return EmailAuthorizationInfo.of(email, code);
    }
}
