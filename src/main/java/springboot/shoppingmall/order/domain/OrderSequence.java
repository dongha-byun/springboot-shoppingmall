package springboot.shoppingmall.order.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "order_sequence")
@Entity
public class OrderSequence {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private Long seq;

    protected OrderSequence(){}

    public static OrderSequence createSequence(LocalDateTime dateTime) {
        OrderSequence orderSequence = new OrderSequence();

        orderSequence.date = convertDateFormat(dateTime);
        orderSequence.seq = 0L;

        return orderSequence;
    }

    private static String convertDateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateTime.format(formatter);
    }

    public String generateOrderCode() {
        return this.date + String.format("%09d", getNextSeq());
    }

    private Long getNextSeq() {
        return ++seq;
    }

    public boolean isNew() {
        return this.id == null && this.seq == 0L;
    }
}
