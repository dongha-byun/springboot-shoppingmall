package springboot.shoppingmall.study;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexStudyTest {

    @Test
    @DisplayName("정규식 학습 테스트")
    void regex_test() {
        // given
        String regex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

        // when
        boolean isMatches = Pattern.matches(regex, "010-1234-1234");

        // then
        assertThat(isMatches).isTrue();
    }
}
