package springboot.shoppingmall.userservice.authentication.email.domain;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Email {
    private String value;

    public Email(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(getValue(), email.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
