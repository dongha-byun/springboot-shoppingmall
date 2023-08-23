package springboot.shoppingmall.study;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocalDateTimeTest {

    @Test
    @DisplayName("LocalDatTime split 테스트")
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

    @Test
    @DisplayName("LocalDateTime 비교 테스트")
    void is_after_is_before_test() {
        // given
        LocalDateTime june = LocalDateTime.of(2023, 6, 1, 20, 0, 0);
        LocalDateTime july = LocalDateTime.of(2023, 7, 1, 20, 0, 0);
        LocalDateTime august = LocalDateTime.of(2023, 8, 1, 20, 0, 0);

        // when
        boolean juneIsAfterJuly = june.isAfter(july);
        boolean julyIsBeforeAugust = july.isBefore(august);

        // then
        assertThat(juneIsAfterJuly).isFalse();
        assertThat(julyIsBeforeAugust).isTrue();
    }
}
