package springboot.shoppingmall.userservice.authentication.email.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MemoryEmailAuthenticationCodeStore implements EmailAuthenticationCodeStore {

    private static final Map<Email, EmailAuthenticationCode> store = new ConcurrentHashMap<>();

    @Override
    public void save(Email email, EmailAuthenticationCode code) {
        store.put(email, code);
    }

    @Override
    public EmailAuthenticationCode getCode(Email email) {
        EmailAuthenticationCode code = store.get(email);
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
