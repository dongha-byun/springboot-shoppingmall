package springboot.shoppingmall.study;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeTest {

    @Test
    void local_date_time_of_string() {
        String dateStr = "2023-04-05";
        String[] split = dateStr.split("-");
        LocalDateTime localDateTime = LocalDateTime.of(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]), 0,0,0);


        assertThat(localDateTime.getYear()).isEqualTo(2023);
        assertThat(localDateTime.getMonthValue()).isEqualTo(4);
        assertThat(localDateTime.getDayOfMonth()).isEqualTo(5);
    }
}
