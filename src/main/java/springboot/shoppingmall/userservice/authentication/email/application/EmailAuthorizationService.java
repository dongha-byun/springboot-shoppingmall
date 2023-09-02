package springboot.shoppingmall.userservice.authentication.email.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthorizationInfo;
import springboot.shoppingmall.userservice.authentication.email.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.userservice.authentication.email.domain.Email;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationCode;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.userservice.authentication.email.domain.EmailAuthorizationProcessor;

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

        return EmailAuthorizationInfo.of(email, authCode, "인증번호가 발송되었습니다.");
    }

    private EmailAuthorizationInfo reCreateCode(Email email, LocalDateTime requestTime) {
        // 1. create code
        String code = codeGenerator.generate();

        // 2. save code
        EmailAuthorizationCode authCode = new EmailAuthorizationCode(code, requestTime);
        store.save(email, authCode);

        // 3. send email
        // 비동기 처리 필요
        emailAuthorizationProcessor.sendAuthorizationMail(email.getValue(), authCode.getValue());

        return EmailAuthorizationInfo.of(email, authCode, "인증번호가 재발송되었습니다.");
    }

    public EmailAuthorizationInfo checkCode(Email email, EmailAuthorizationCode code, LocalDateTime checkRequestTime) {
        EmailAuthorizationCode findCode = store.getCode(email);
        if(checkRequestTime.isAfter(findCode.getExpireTime())) {
            return reCreateCode(email, checkRequestTime);
        }

        if(!findCode.equals(code)) {
            throw new IllegalArgumentException("인증번호가 맞지 않습니다.");
        }
        store.remove(email);
        return EmailAuthorizationInfo.of(email, code, "인증이 완료되었습니다.");
    }


}
