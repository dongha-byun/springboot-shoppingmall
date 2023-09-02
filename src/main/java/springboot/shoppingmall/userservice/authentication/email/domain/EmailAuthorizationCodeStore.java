package springboot.shoppingmall.userservice.authentication.email.domain;

public interface EmailAuthorizationCodeStore {
    void save(Email email, EmailAuthorizationCode code);
    EmailAuthorizationCode getCode(Email email);
    void remove(Email email);
}
