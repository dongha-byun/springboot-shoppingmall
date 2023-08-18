package springboot.shoppingmall.authorization.domain;

public interface EmailSender {

    void send(String email, String title, String message);
}
