package springboot.shoppingmall.authorization.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MemoryEmailAuthorizationCodeStore implements EmailAuthorizationCodeStore{

    private static final Map<Email, EmailAuthorizationCode> store = new ConcurrentHashMap<>();

    @Override
    public void save(Email email, EmailAuthorizationCode code) {
        store.put(email, code);
    }

    @Override
    public EmailAuthorizationCode getCode(Email email) {
        EmailAuthorizationCode code = store.get(email);
        if(code == null) {
            throw new IllegalArgumentException("인증번호가 존재하지 않습니다.");
        }
        return code;
    }

    @Override
    public void remove(Email email) {
        store.remove(email);
    }
}
