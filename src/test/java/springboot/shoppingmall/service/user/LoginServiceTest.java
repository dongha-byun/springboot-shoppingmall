package springboot.shoppingmall.service.user;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.LoginRequest;
import springboot.shoppingmall.dto.user.LoginResponse;
import springboot.shoppingmall.repository.user.UserRepository;

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
        LoginResponse login = loginService.login(new LoginRequest("admin", "admin1!"));

        // then
        assertThat(login.getUserId()).isEqualTo(saveUser.getId());
    }

}