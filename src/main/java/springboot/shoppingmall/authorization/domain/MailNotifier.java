package springboot.shoppingmall.authorization.domain;

public interface MailNotifier {
    void send(String email, String title, String message);
}
