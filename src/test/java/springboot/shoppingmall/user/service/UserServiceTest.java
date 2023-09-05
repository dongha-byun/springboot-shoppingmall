package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserGrade;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.service.dto.FindEmailRequestDto;
import springboot.shoppingmall.user.service.dto.FindEmailResultDto;
import springboot.shoppingmall.user.service.dto.UserCreateDto;
import springboot.shoppingmall.user.service.dto.UserDto;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    LocalDateTime signUpDate = LocalDateTime.of(2023, 5, 4, 12, 30, 11);

    @DisplayName("회원을 등록한다.")
    @Test
    void save_user() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto(
                "테스터", "tester@test.com", "a", "a",
                "010-2222-3333", signUpDate
        );

        // when
        UserDto userDto = userService.signUp(userCreateDto);

        // then
        assertAll(
                () -> assertThat(userDto.getId()).isNotNull(),
                () -> assertThat(userDto.getName()).isEqualTo("테스터"),
                () -> assertThat(userDto.getEmail()).isEqualTo("tester@test.com"),
                () -> assertThat(userDto.getTelNo()).isEqualTo("010-2222-3333"),
                () -> assertThat(userDto.getSignUpDate()).isEqualTo(LocalDateTime.of(2023, 5, 4, 12, 30, 11))
        );
    }

    @Test
    @DisplayName("이미 가입이 된 이메일 정보로는 가입할 수 없다.")
    void duplicate_email_fail() {
        // given
        userRepository.save(
                new User("기존 가입자", "user@test.com", "user1!", "010-1234-1234")
        );

        // when & then
        UserCreateDto userCreateDto = new UserCreateDto("신규 가입자", "user@test.com",
                "user1!", "user1!", "010-1235-1235", LocalDateTime.now());

        assertThatIllegalArgumentException().isThrownBy(
                () -> userService.signUp(userCreateDto)
        ).withMessageContaining("이미 가입된 정보가 있습니다.");
    }

    @DisplayName("비밀번호와 비밀번호 확인 정보가 다른 경우, 회원가입이 불가능하다.")
    @Test
    void sign_up_fail_with_password_confirmPassword_not_collect() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto(
                "테스터", "tester", "a", "b",
                "010-2222-3333", signUpDate
        );

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> userService.signUp(userCreateDto)
        );
    }

    @Test
    @DisplayName("이름과 연락처 정보를 통해 마스킹된 이메일을 찾는다.")
    void find_email() {
        // given
        userRepository.save(
                new User("사용자1", "user1@test.com", "test1!", "010-2222-3333")
        );

        // when
        FindEmailRequestDto findEmailRequestDto = new FindEmailRequestDto("사용자1", "010-2222-3333");
        FindEmailResultDto resultDto = userService.findEmail(findEmailRequestDto);

        // then
        assertThat(resultDto.getEmail()).isEqualTo("us***@test.com");
    }

    @Test
    @DisplayName("비밀번호 찾기")
    void findPw(){
        // given
        UserCreateDto userCreateDto = new UserCreateDto(
                "테스터", "tester@test.com", "a", "a",
                "010-2222-3333", signUpDate
        );
        userService.signUp(userCreateDto);

        // when
        FindPwResponse response = userService.findPw("테스터", "010-2222-3333", "tester@test.com");

        // then
        assertThat(response.getEmail()).isEqualTo("tester@test.com");
    }

    @Test
    @DisplayName("사용자 정보를 변경한다.")
    void editUserTest() {
        // given
        User user = userRepository.save(new User("사용자1", "user1@test.com", "user1!", "010-1111-2222"));

        // when
        UserEditRequest userEditRequest = new UserEditRequest("user2@", "010-2222-4444");
        userService.editUser(user.getId(), userEditRequest);

        // then
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(findUser.getUserName()).isEqualTo("사용자1");
        assertThat(findUser.getEmail()).isEqualTo("user1@test.com");
        assertThat(findUser.getPassword()).isEqualTo("user2@");
        assertThat(findUser.telNo()).isEqualTo("010-2222-4444");
    }

    @Test
    @DisplayName("다음 회원등급 승급까지 남은 주문량/주문금액을 조회한다.")
    void get_next_user_grade_condition() {
        // given
        LocalDateTime signUpDate = LocalDateTime.of(2022, 5, 7, 11, 0);
        User user = userRepository.save(
                new User(
                        "사용자1", "user1", "user1!",
                        "010-1111-2222", signUpDate
                )
        );

        // when
        UserGradeInfoDto userGradeInfo = userService.getUserGradeInfo(user.getId());

        // then
        assertThat(userGradeInfo.getUserId()).isEqualTo(user.getId());
        assertThat(userGradeInfo.getUserName()).isEqualTo(user.getUserName());
        assertThat(userGradeInfo.getSignUpDate()).isEqualTo(signUpDate);
        assertThat(userGradeInfo.getCurrentUserGrade()).isEqualTo(UserGrade.NORMAL);
        assertThat(userGradeInfo.getNextUserGrade()).isEqualTo(UserGrade.REGULAR);
        assertThat(userGradeInfo.getOrderCount()).isEqualTo(0);
        assertThat(userGradeInfo.getAmount()).isEqualTo(0);
    }
}