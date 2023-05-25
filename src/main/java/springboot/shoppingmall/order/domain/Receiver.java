package springboot.shoppingmall.order.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Receiver {
    private String name;

    public Receiver(String name) {
        this.name = name;
    }
}
