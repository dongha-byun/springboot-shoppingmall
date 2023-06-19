package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    @DisplayName("특정 등급 이상의 사용자를 조회")
    void find_user_by_over_the_grade() {
        // given
        User normalUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", 0, false,
                        new UserGradeInfo(UserGrade.NORMAL, 0, 0))
        );
        User regularUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", 0, false,
                        new UserGradeInfo(UserGrade.REGULAR, 10, 50000))
        );
        User vipUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", 0, false,
                        new UserGradeInfo(UserGrade.VIP, 50, 150000))
        );

        // when
        List<User> userList = userFinder.findUserOverTheUserGrade(UserGrade.REGULAR);

        // then
        assertThat(userList).hasSize(2);
        List<Long> ids = userList.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactly(
                regularUser.getId(), vipUser.getId()
        );
    }
}