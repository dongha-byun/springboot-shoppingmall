package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserFinderTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserFinder userFinder;

    @Test
    @DisplayName("사용자 조회 테스트")
    void findUserTest(){
        // given
        User saveUser = userRepository.save(new User("임시사용자1", "tempUser1", "tempUser1!", "010-1234-1234"));

        // when
        User findUser = userFinder.findUserById(saveUser.getId());

        // then
        assertThat(findUser).isEqualTo(saveUser);
    }

    @Test
    @DisplayName("로그인 아이디로 회원 조회")
    void find_user_by_login_id() {
        // given
        User saveUser = userRepository.save(new User("임시사용자1", "tempUser1", "tempUser1!", "010-1234-1234"));

        // when
        User findUser = userFinder.findUserByLoginId(saveUser.getLoginId());

        // then
        assertThat(findUser).isEqualTo(saveUser);
    }

}