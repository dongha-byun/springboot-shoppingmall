package springboot.shoppingmall.userservice.authentication.email.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailAuthenticationCodeTest {

    @Test
    @DisplayName("현재 코드가 유효한지 시간을 통해 확인한다.")
    void is_valid_at_time() {
        // given
        EmailAuthenticationCode emailAuthenticationCode = new EmailAuthenticationCode(
                "012345", LocalDateTime.of(2023, 8, 29, 12, 0, 0)
        );

        // when
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 29, 12, 4, 1);
        boolean expectTrue = emailAuthenticationCode.isValidAt(requestTime);

        // then
        assertThat(expectTrue).isTrue();
    }

    @Test
    @DisplayName("인증번호 생성 시점보다 5분이 지나면, 해당 코드는 유효하지 않다고 판단한다.")
    void is_not_valid_at_time() {
        // given
        EmailAuthenticationCode emailAuthenticationCode = new EmailAuthenticationCode(
                "012345", LocalDateTime.of(2023, 8, 29, 12, 0, 0)
        );

        // when
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 29, 12, 5, 1);
        boolean expectTrue = emailAuthenticationCode.isValidAt(requestTime);

        // then
        assertThat(expectTrue).isFalse();
    }
}