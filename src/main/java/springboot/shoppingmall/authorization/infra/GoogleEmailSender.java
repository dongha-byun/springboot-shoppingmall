package springboot.shoppingmall.authorization.infra;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.authorization.domain.EmailSender;

@RequiredArgsConstructor
@Component
public class GoogleEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void send(String email, String title, String content) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("byundongha@gmail.com"));
            mimeMessage.setSubject(title);
            mimeMessage.setText(content);
        };

        try{
            mailSender.send(preparator);
        }catch (MailException ex) {
            ex.printStackTrace();
        }
    }
}
