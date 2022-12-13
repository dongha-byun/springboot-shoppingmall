package springboot.shoppingmall.authorization.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.LoginRequest;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("로그인 인증 토근 생성")
    void authorizationSuccessTest(){
        // given
        userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));

        // when
        TokenResponse tokenResponse = authService.login(new LoginRequest("test", "test1!"));

        // then
        assertThat(tokenResponse.getToken()).isNotNull();
    }
}