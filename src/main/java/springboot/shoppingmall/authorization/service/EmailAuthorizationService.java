package springboot.shoppingmall.authorization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.authorization.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.authorization.domain.Email;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationProcessor;

@RequiredArgsConstructor
@Service
public class EmailAuthorizationService {
    private final EmailAuthorizationCodeStore store;
    private final EmailAuthorizationProcessor emailAuthorizationProcessor;
    private final AuthorizationCodeGenerator codeGenerator;

    public void createCode(Email email) {
        // 1. create code
        String code = codeGenerator.generate();

        // 2. save code
        EmailAuthorizationCode authCode = new EmailAuthorizationCode(code);
        store.save(email, authCode);

        // 3. send email
        emailAuthorizationProcessor.sendAuthorizationMail(email.getValue(), authCode.getValue());
    }
}
