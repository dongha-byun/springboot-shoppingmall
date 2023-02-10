package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void editUserTest() {
        // 사용자 정보에서 이름/아이디 는 변경할 수 없다.
        // given
        User user = new User("사용자1", "user1", "user1!", "010-1111-2222");

        // when
        User edit = new User("사용자2", "user2", "user2@", "010-2222-3333");
        user.updateUser(edit);

        // then
        assertThat(user.getUserName()).isEqualTo("사용자1");
        assertThat(user.getLoginId()).isEqualTo("user1");
        assertThat(user.getPassword()).isEqualTo("user2@");
        assertThat(user.getTelNo()).isEqualTo("010-2222-3333");
    }
}