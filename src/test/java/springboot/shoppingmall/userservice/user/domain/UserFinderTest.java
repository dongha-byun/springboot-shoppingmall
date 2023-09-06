package springboot.shoppingmall.userservice.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
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
        User findUser = userFinder.findUserByLoginId(saveUser.getEmail());

        // then
        assertThat(findUser).isEqualTo(saveUser);
    }

    @Test
    @DisplayName("특정 등급 이상의 사용자를 조회")
    void find_user_by_over_the_grade() {
        // given
        LocalDateTime signUpDate = LocalDateTime.of(2023, 1, 5, 9, 15, 0);
        User normalUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", signUpDate,
                        0, false,
                        new UserGradeInfo(UserGrade.NORMAL, 0, 0))
        );
        User regularUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", signUpDate,
                        0, false,
                        new UserGradeInfo(UserGrade.REGULAR, 10, 50000))
        );
        User vipUser = userRepository.save(
                new User("일반회원테스터", "normal_tester", "normal_tester_!",
                        "010-2222-3333", signUpDate,
                        0, false,
                        new UserGradeInfo(UserGrade.VIP, 50, 150000))
        );

        // when
        List<User> userList = userFinder.findUserOverTheUserGrade(UserGrade.REGULAR);

        // then
        assertThat(userList)
                .hasSize(2)
                .extracting("id", "userGradeInfo.grade")
                .containsExactly(
                        tuple(regularUser.getId(), UserGrade.REGULAR),
                        tuple(vipUser.getId(), UserGrade.VIP)
                );
    }

    @Test
    @DisplayName("이름과 연락처로 가입된 이메일 정보를 조회한다.")
    void find_email_by_name_telNo() {
        // given
        User savedUser = userRepository.save(
                new User(
                        "가입사용자", "addUser@test.com", "test1!", "010-2233-4455"
                )
        );

        // when
        User findUser = userFinder.findEmailByNameAndTelNo("가입사용자", "010-2233-4455");

        // then
        assertThat(findUser.getId()).isEqualTo(savedUser.getId());
    }
}