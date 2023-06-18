package springboot.shoppingmall.coupon.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsingDurationTest {

    @Test
    @DisplayName("사용기한에서 시작일이 종료일보다 클 수 없다.")
    void validate_using_duration_fromDate_toDate() {
        // given
        LocalDateTime fromDate = LocalDateTime.of(2023, 6, 6, 0, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2023, 6, 5, 23, 59, 59);

        // when & thin
        assertThatIllegalArgumentException().isThrownBy(
                () -> new UsingDuration(fromDate, toDate)
        );
    }
}