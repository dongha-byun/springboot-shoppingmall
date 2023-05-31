package springboot.shoppingmall.message;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageProvider {

    private final MessageSource messageSource;

    public String getMessage(String code) {
        return getMessage(code, null, null);
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }
}
