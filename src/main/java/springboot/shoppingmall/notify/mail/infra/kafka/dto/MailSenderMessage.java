package springboot.shoppingmall.notify.mail.infra.kafka.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MailSenderMessage implements Serializable {
    private String email;
    private String title;
    private String content;
}
