package springboot.shoppingmall.userservice.authentication.email.domain;

public interface MailNotifier {
    void send(String email, String title, String message);
}
