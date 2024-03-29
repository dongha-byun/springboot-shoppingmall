package springboot.shoppingmall.utils;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MaskingUtilTest {

    @ParameterizedTest
    @CsvSource(value = {"마스크:마*크", "강남역출구:강***구"}, delimiterString = ":")
    @DisplayName("문자열 마스킹 처리")
    void masking(String input, String result){
        // given

        // when
        String masking = MaskingUtil.maskingName(input);

        // then
        assertThat(masking).isEqualTo(result);
    }

    @Test
    @DisplayName("이메일은 아이디 앞 2자리 제외 모두 마스킹하고, 주소는 모두 노출시킨다.")
    void email_masking() {
        // given
        String email = "user1@test.com";

        // when
        String result = MaskingUtil.maskingEmail(email);

        // then
        assertThat(result).isEqualTo("us***@test.com");
    }

}