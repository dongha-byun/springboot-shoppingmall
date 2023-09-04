package springboot.shoppingmall.userservice.authentication.email.domain;

public interface EmailAuthenticationCodeStore {
    void save(Email email, EmailAuthenticationCode code);
    EmailAuthenticationCode getCode(Email email);
    void remove(Email email);

    void clear();
}
