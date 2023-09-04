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
        return store.get(email);
    }

    @Override
    public void remove(Email email) {
        store.remove(email);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
