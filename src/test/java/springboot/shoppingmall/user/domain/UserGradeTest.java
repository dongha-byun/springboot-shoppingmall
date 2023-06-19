package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserGradeTest {

    @Test
    @DisplayName("특정 등급 이상 등급들 조회")
    void find_grades_of_over_the_target_grade() {
        // given
        UserGrade regular = UserGrade.REGULAR;

        // when
        List<UserGrade> userGrades = regular.overGrades();

        // then
        assertThat(userGrades).containsExactly(
                UserGrade.REGULAR,
                UserGrade.VIP,
                UserGrade.VVIP
        );
    }

}