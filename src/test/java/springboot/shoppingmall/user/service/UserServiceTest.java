package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwRequest;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.SignUpRequest;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserRequest;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.service.UserService;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void signUpTest(){
        // given
        SignUpRequest signUpRequest = new SignUpRequest("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        UserResponse userResponse = userService.signUp(signUpRequest);

        // then
        assertAll(
                () -> assertThat(userResponse.getName()).isEqualTo("변동하"),
                () -> assertThat(userResponse.getLoginId()).isEqualTo("dongha"),
                () -> assertThat(userResponse.getTelNo()).isEqualTo("010-1234-1234")
        );
    }

    @Test
    @DisplayName("아이디 조회 성공")
    void findId(){
        // given
        SignUpRequest signUpRequest = new SignUpRequest("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");
        userService.signUp(signUpRequest);

        // when
        FindIdResponse response = userService.findId(new FindIdRequest("변동하", "010-1234-1234"));

        // then
        assertThat(response.getLoginId()).isEqualTo("dongha");
    }

    @Test
    @DisplayName("비밀번호 조회 성공")
    void findPw(){
        // given
        SignUpRequest signUpRequest = new SignUpRequest("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");
        userService.signUp(signUpRequest);

        // when
        FindPwResponse response = userService.findPw(new FindPwRequest("변동하", "010-1234-1234", "dongha"));

        // then
        assertThat(response.getLoginId()).isEqualTo("dongha");
    }

    @Test
    @DisplayName("사용자 정보 변경 테스트")
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
        assertThat(findUser.getTelNo()).isEqualTo("010-2222-4444");
    }
}