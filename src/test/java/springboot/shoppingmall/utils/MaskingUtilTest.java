package springboot.shoppingmall.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MaskingUtilTest {

    @ParameterizedTest
    @CsvSource(value = {"변동하:변*하", "김수연바보:김***보"}, delimiterString = ":")
    @DisplayName("문자열 마스킹 처리")
    void masking(String input, String result){
        // given

        // when
        String masking = MaskingUtil.maskString(input);

        // then
        Assertions.assertThat(masking).isEqualTo(result);
    }

}