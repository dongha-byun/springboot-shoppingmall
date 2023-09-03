package springboot.shoppingmall.userservice.authentication.email.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailAuthenticationCode {
    private String value;
    private LocalDateTime expireTime;

    public EmailAuthenticationCode(String value) {
        this.value = value;
    }

    public EmailAuthenticationCode(String value, LocalDateTime requestTime) {
        this.value = value;
        this.expireTime = requestTime.plusMinutes(5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailAuthenticationCode that = (EmailAuthenticationCode) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    public boolean isValidAt(LocalDateTime requestTime) {
        return this.expireTime.isAfter(requestTime);
    }
}
