package springboot.shoppingmall.notify.mail.infra.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.notify.mail.infra.kafka.dto.MailSenderMessage;
import springboot.shoppingmall.userservice.authentication.email.domain.MailNotifier;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMailNotifier implements MailNotifier {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String NOTIFY_MAIL_TOPIC = "notify_mail_topic";

    @Override
    public void send(String email, String title, String message) {
        try {
            MailSenderMessage mailSenderMessage = new MailSenderMessage(email, title, message);
            String jsonData = objectMapper.writeValueAsString(mailSenderMessage);

            kafkaTemplate.send(NOTIFY_MAIL_TOPIC, jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
