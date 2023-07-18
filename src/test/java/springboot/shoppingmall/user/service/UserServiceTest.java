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
import springboot.shoppingmall.user.controller.request.SignUpRequest;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.service.dto.UserCreateDto;

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
                "테스터", "tester", "a", "a",
                "010-2222-3333", signUpDate
        );

        // when
        UserResponse userResponse = userService.signUp(userCreateDto);

        // then
        assertAll(
                () -> assertThat(userResponse.getName()).isEqualTo("테스터"),
                () -> assertThat(userResponse.getLoginId()).isEqualTo("tester"),
                () -> assertThat(userResponse.getTelNo()).isEqualTo("010-2222-3333"),
                () -> assertThat(userResponse.getSignUpDate()).isEqualTo("2023-05-04")
        );
    }

    @Test
    @DisplayName("아이디 찾기")
    void findId(){
        // given
        UserCreateDto userCreateDto = new UserCreateDto(
                "테스터", "tester", "a", "a",
                "010-2222-3333", signUpDate
        );
        userService.signUp(userCreateDto);

        // when
        FindIdResponse response = userService.findId("테스터", "010-2222-3333");

        // then
        assertThat(response.getLoginId()).isEqualTo("te****");
    }

    @Test
    @DisplayName("비밀번호 찾기")
    void findPw(){
        // given
        UserCreateDto userCreateDto = new UserCreateDto(
                "테스터", "tester", "a", "a",
                "010-2222-3333", signUpDate
        );
        userService.signUp(userCreateDto);

        // when
        FindPwResponse response = userService.findPw("테스터", "010-2222-3333", "tester");

        // then
        assertThat(response.getLoginId()).isEqualTo("tester");
    }

    @Test
    @DisplayName("사용자 정보를 변경한다.")
    void editUserTest() {
        // given
        User user = userRepository.save(new User("사용자1", "user1", "user1!", "010-1111-2222"));

        // when
        UserEditRequest userEditRequest = new UserEditRequest("user2@", "010-2222-4444");
        userService.editUser(user.getId(), userEditRequest);

        // then
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(findUser.getUserName()).isEqualTo("사용자1");
        assertThat(findUser.getLoginId()).isEqualTo("user1");
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