package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TelNoTest {

    @Test
    @DisplayName("연락처 형태로 데이터가 잘 생성되는지 검증")
    void telNo_validate_test() {
        // 휴대폰 번호 형태의 데이터가 맞는지 검증
        // 000-0000-0000 or 000-000-0000
        // 여기서 맨 앞은 010, 011, 016, 017, 018, 019 만 가능

        // given
        String input = "010-1234-1234";

        // when
        TelNo telNo = new TelNo(input);

        // then
        assertThat(telNo).isNotNull();
    }

    @Test
    @DisplayName("00-0000-0000 형태의 데이터는 객체를 생성할 수 없다.")
    void telNo_validate_fail_test() {
        // given
        String input = "02-1234-1233";

        // 생성자에서 검증
        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new TelNo(input)
        );
    }

    @Test
    @DisplayName("000-000-0000 형태의 데이터는 앞 자리가 011, 016, 017, 018, 019 여야 한다.")
    void telNo_validate_fail_test2() {
        // given
        String input = "010-123-1234";

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new TelNo(input)
        );
    }
}