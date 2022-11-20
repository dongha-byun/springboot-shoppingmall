package springboot.shoppingmall.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindIdResponse;
import springboot.shoppingmall.dto.user.SignUpRequest;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUpTest(){
        // given
        SignUpRequest signUpRequest = new SignUpRequest("변동하", "dongha", "dongha1!", "dongha1!", "010-1234-1234");

        // when
        Long id = userService.signUp(signUpRequest);

        // then
        User user = userService.findUser(id);
        assertAll(
                () -> assertThat(user.getUserName()).isEqualTo("변동하"),
                () -> assertThat(user.getLoginId()).isEqualTo("dongha"),
                () -> assertThat(user.getPassword()).isEqualTo("dongha1!"),
                () -> assertThat(user.getTelNo()).isEqualTo("010-1234-1234")
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
}