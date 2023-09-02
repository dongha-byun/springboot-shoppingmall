package springboot.shoppingmall.userservice.authentication.email.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailAuthorizationCode {
    private String value;
    private LocalDateTime expireTime;

    public EmailAuthorizationCode(String value) {
        this.value = value;
    }

    public EmailAuthorizationCode(String value, LocalDateTime requestTime) {
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
        EmailAuthorizationCode that = (EmailAuthorizationCode) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
