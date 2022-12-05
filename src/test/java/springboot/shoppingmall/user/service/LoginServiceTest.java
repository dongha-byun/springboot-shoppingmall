package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.dto.LoginRequest;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.service.LoginService;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("로그인 처리가 성공")
    void loginSuccess(){
        // given
        User saveUser = userRepository.save(
                new User("관리자", "admin", "admin1!", "010-0000-0000")
        );

        // when
        User user = loginService.login(new LoginRequest("admin", "admin1!"));

        // then
        assertThat(user.getId()).isEqualTo(saveUser.getId());
    }

}