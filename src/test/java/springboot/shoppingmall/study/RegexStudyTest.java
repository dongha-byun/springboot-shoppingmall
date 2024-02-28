package springboot.shoppingmall.study;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class RegexStudyTest {

    @Test
    @DisplayName("핸드폰 번호 정규식")
    void regex_telNo() {
        // given
        String regex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

        // when
        boolean isMatches = Pattern.matches(regex, "010-1234-1234");

        // then
        assertThat(isMatches).isTrue();
    }

    @Test
    @DisplayName("img 태그 정규식")
    void regex_img_tag() {
        // given
        String tempContentImageURL = "http://localhost:8000/content/img/temp/";
        String regex = "<img[^>]*src=[\"']?"+tempContentImageURL+"([^>\"']+)[\"']?[^>]*>";
        log.info("regex = {}", regex);
        Pattern pattern = Pattern.compile(regex);

        // when
        String detail = "<p>"
                + "<img src=\"http://localhost:8000/content/img/temp/aa8eda55-501d-48e4-ba76-1591f5422932\">"
                + "</p>"
                + "<p>"
                + "<img src=\"http://localhost:8000/content/img/temp/d73d13d6-02d6-4174-9b48-615aa5ec7aa2\">"
                + "</p>";
        Matcher matcher = pattern.matcher(detail);

        List<String> imgTags = new ArrayList<>();
        while(matcher.find()) {
            imgTags.add(matcher.group(1));
        }

        String newDetail = detail.replaceAll("/content/img/temp", "/content/img/prod");
        log.info("newDetail => {}", newDetail);

        // then
        assertThat(imgTags).hasSize(2);
        assertThat(imgTags).containsExactly(
                "aa8eda55-501d-48e4-ba76-1591f5422932", "d73d13d6-02d6-4174-9b48-615aa5ec7aa2"
        );
    }
}
