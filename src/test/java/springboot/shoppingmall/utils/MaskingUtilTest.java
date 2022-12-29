package springboot.shoppingmall.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MaskingUtilTest {

    @ParameterizedTest
    @CsvSource(value = {"마스크:마*크", "강남역출구:강***구"}, delimiterString = ":")
    @DisplayName("문자열 마스킹 처리")
    void masking(String input, String result){
        // given

        // when
        String masking = MaskingUtil.maskString(input);

        // then
        Assertions.assertThat(masking).isEqualTo(result);
    }

}