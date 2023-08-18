package springboot.shoppingmall.authorization.configuration;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
public class AuthorizationMailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("side.project.shoppingmall@gmail.com");
        javaMailSender.setPassword("tkdlemvmfhwprxm12!@");
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setPort(587);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
